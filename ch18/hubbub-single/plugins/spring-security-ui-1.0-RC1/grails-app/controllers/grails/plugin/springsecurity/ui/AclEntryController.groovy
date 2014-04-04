/* Copyright 2009-2013 SpringSource.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package grails.plugin.springsecurity.ui

import org.springframework.dao.DataIntegrityViolationException

/**
 * @author <a href='mailto:burt@burtbeckwith.com'>Burt Beckwith</a>
 */
class AclEntryController extends AbstractS2UiController {

	def aclPermissionFactory

	def create() {
		def aclEntry = lookupClass().newInstance(params)
		aclEntry.granting = true
		[aclEntry: aclEntry, sids: lookupAclSidClass().list()]
	}

	def save() {
		def aclEntry = lookupClass().newInstance(params)
		if (!aclEntry.save(flush: true)) {
			render view: 'create', model: [aclEntry: aclEntry, sids: lookupAclSidClass().list()]
			return
		}

		flash.message = "${message(code: 'default.created.message', args: [message(code: 'aclEntry.label', default: 'AclEntry'), aclEntry.id])}"
		redirect action: 'edit', id: aclEntry.id
	}

	def edit() {
		def aclEntry = findById()
		if (!aclEntry) return

		[aclEntry: aclEntry, sids: lookupAclSidClass().list()]
	}

	def update() {

		def aclEntry = findById()
		if (!aclEntry) return
		if (!versionCheck('aclEntry.label', 'AclEntry', aclEntry, [aclEntry: aclEntry])) {
			return
		}

		Long parentId = params.parent?.id ? params.parent.id.toLong() : null
		Long ownerId = params.owner?.id ? params.owner.id.toLong() : null
		if (!springSecurityUiService.updateAclEntry(aclEntry, params.aclObjectIdentity.id.toLong(),
				params.sid.id.toLong(), params.int('aceOrder'), params.int('mask'),
				params.granting == 'on', params.auditSuccess == 'on', params.auditFailure == 'on')) {
			render view: 'edit', model: [aclEntry: aclEntry, sids: lookupAclSidClass().list()]
			return
		}

		flash.message = "${message(code: 'default.updated.message', args: [message(code: 'aclEntry.label', default: 'AclEntry'), aclEntry.id])}"
		redirect action: 'edit', id: aclEntry.id
	}

	def delete() {
		def aclEntry = findById()
		if (!aclEntry) return

		try {
			springSecurityUiService.deleteAclEntry aclEntry
			flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'aclEntry.label', default: 'AclEntry'), params.id])}"
			redirect action: 'search'
		}
		catch (DataIntegrityViolationException e) {
			flash.error = "${message(code: 'default.not.deleted.message', args: [message(code: 'aclEntry.label', default: 'AclEntry'), params.id])}"
			redirect action: 'edit', id: params.id
		}
	}

	def search() {
		[granting: 0, auditSuccess: 0, auditFailure: 0, sids: lookupAclSidClass().list()]
	}

	def aclEntrySearch() {
		boolean useOffset = params.containsKey('offset')
		setIfMissing 'max', 10, 100
		setIfMissing 'offset', 0

		def hql = new StringBuilder('FROM ').append(lookupClassName()).append(' e WHERE 1=1 ')
		def queryParams = [:]

		for (name in ['aceOrder', 'mask']) {
			if (params[name]) {
				hql.append " AND e.${name}=:$name"
				queryParams[name] = params.int(name)
			}
		}

		for (name in ['sid', 'aclObjectIdentity']) {
			if (params[name] && params[name] != 'null') {
				hql.append " AND e.${name}.id=:$name"
				queryParams[name] = params.long(name)
			}
		}

		for (name in ['granting', 'auditSuccess', 'auditFailure']) {
			Integer value = params.int(name)
			if (value) {
				hql.append " AND e.$name=:$name"
				queryParams[name] = value == 1
			}
		}

		if (params.aclClass) {
			// special case for external search
			hql.append " AND e.aclObjectIdentity.aclClass.id=:aclClass"
			queryParams.aclClass = params.aclClass.toLong()
		}

		int totalCount = lookupClass().executeQuery("SELECT COUNT(DISTINCT e) $hql", queryParams)[0]

		Integer max = params.int('max')
		Integer offset = params.int('offset')

		String orderBy = ''
		if (params.sort) {
			orderBy = " ORDER BY e.$params.sort ${params.order ?: 'ASC'}"
		}

		def results = lookupClass().executeQuery(
				"SELECT DISTINCT e $hql $orderBy",
				queryParams, [max: max, offset: offset])
		def model = [results: results, totalCount: totalCount, searched: true,
		             sids: lookupAclSidClass().list(), permissionFactory: aclPermissionFactory]
		// add query params to model for paging
		for (name in ['granting', 'auditSuccess', 'auditFailure', 'sid',
		              'aclObjectIdentity', 'aceOrder', 'mask']) {
		 	model[name] = params[name]
		}

		render view: 'search', model: model
	}

	protected findById() {
		def aclEntry = lookupClass().get(params.id)
		if (!aclEntry) {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'aclEntry.label', default: 'AclEntry'), params.id])}"
			redirect action: 'search'
		}

		aclEntry
	}

	protected String lookupClassName() {
		'grails.plugin.springsecurity.acl.AclEntry'
	}

	protected Class<?> lookupClass() {
		grailsApplication.getDomainClass(lookupClassName()).clazz
	}

	protected Class<?> lookupAclSidClass() {
		grailsApplication.getDomainClass('grails.plugin.springsecurity.acl.AclSid').clazz
	}
}
