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
class AclObjectIdentityController extends AbstractS2UiController {

	def create() {
		[aclObjectIdentity: lookupClass().newInstance(params),
		 classes: lookupAclClassClass().list(), sids: lookupAclSidClass().list()]
	}

	def save() {
		def aclObjectIdentity = lookupClass().newInstance(params)
		if (!aclObjectIdentity.save(flush: true)) {
			render view: 'create', model: [aclObjectIdentity: aclObjectIdentity,
			                               classes: lookupAclClassClass().list(),
			                               sids: lookupAclSidClass().list()]
			return
		}

		flash.message = "${message(code: 'default.created.message', args: [message(code: 'aclObjectIdentity.label', default: 'AclObjectIdentity'), aclObjectIdentity.id])}"
		redirect action: 'edit', id: aclObjectIdentity.id
	}

	def edit() {
		def aclObjectIdentity = findById()
		if (!aclObjectIdentity) return

		[aclObjectIdentity: aclObjectIdentity,
		 classes: lookupAclClassClass().list(),
		 sids: lookupAclSidClass().list()]
	}

	def update() {

		def aclObjectIdentity = findById()
		if (!aclObjectIdentity) return
		if (!versionCheck('aclObjectIdentity.label', 'AclObjectIdentity', aclObjectIdentity, [aclObjectIdentity: aclObjectIdentity])) {
			return
		}

		Long parentId = params.parent?.id ? params.parent.id.toLong() : null
		Long ownerId = params.owner?.id ? params.owner.id.toLong() : null
		if (!springSecurityUiService.updateAclObjectIdentity(aclObjectIdentity, params.long('objectId'),
				params.aclClass.id.toLong(), parentId, ownerId, params.entriesInheriting == 'on')) {
			render view: 'edit', model: [aclObjectIdentity: aclObjectIdentity,
			                             classes: lookupAclClassClass().list(),
			                             sids: lookupAclSidClass().list()]
			return
		}

		flash.message = "${message(code: 'default.updated.message', args: [message(code: 'aclObjectIdentity.label', default: 'AclObjectIdentity'), aclObjectIdentity.id])}"
		redirect action: 'edit', id: aclObjectIdentity.id
	}

	def delete() {
		def aclObjectIdentity = findById()
		if (!aclObjectIdentity) return

		try {
			springSecurityUiService.deleteAclObjectIdentity aclObjectIdentity
			flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'aclObjectIdentity.label', default: 'AclObjectIdentity'), params.id])}"
			redirect action: 'search'
		}
		catch (DataIntegrityViolationException e) {
			flash.error = "${message(code: 'default.not.deleted.message', args: [message(code: 'aclObjectIdentity.label', default: 'AclObjectIdentity'), params.id])}"
			redirect action: 'edit', id: params.id
		}
	}

	def search() {
		[entriesInheriting: 0,
		 classes: lookupAclClassClass().list(),
		 sids: lookupAclSidClass().list()]
	}

	def aclObjectIdentitySearch() {

		boolean useOffset = params.containsKey('offset')
		setIfMissing 'max', 10, 100
		setIfMissing 'offset', 0

		def hql = new StringBuilder('FROM ').append(lookupClassName()).append(' oid WHERE 1=1 ')
		def queryParams = [:]

		if (params.objectId) {
			hql.append " AND oid.objectId=:objectId"
			queryParams.objectId = params.long('objectId')
		}

		for (name in ['ownerSid', 'parent', 'aclClass']) {
			if (params[name] && params[name] != 'null') {
				long id = params.long(name)
				if (name == 'ownerSid') name = 'owner'
				hql.append " AND oid.${name}.id=:$name"
				queryParams[name] = id
			}
		}

		for (name in ['entriesInheriting']) {
			Integer value = params.int(name)
			if (value) {
				hql.append " AND oid.$name=:$name"
				queryParams[name] = value == 1
			}
		}

		int totalCount = lookupClass().executeQuery("SELECT COUNT(DISTINCT oid) $hql", queryParams)[0]

		Integer max = params.int('max')
		Integer offset = params.int('offset')

		String orderBy = ''
		if (params.sort) {
			orderBy = " ORDER BY oid.$params.sort ${params.order ?: 'ASC'}"
		}

		def results = lookupClass().executeQuery(
				"SELECT DISTINCT oid $hql $orderBy",
				queryParams, [max: max, offset: offset])
		def model = [results: results, totalCount: totalCount, searched: true,
		             classes: lookupAclClassClass().list(), sids: lookupAclSidClass().list()]

		// add query params to model for paging
		for (name in ['aclClass', 'objectId', 'entriesInheriting', 'ownerSid', 'parent']) {
		 	model[name] = params[name]
		}

		render view: 'search', model: model
	}

	protected findById() {
		def aclObjectIdentity = lookupClass().get(params.id)
		if (!aclObjectIdentity) {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'aclObjectIdentity.label', default: 'AclObjectIdentity'), params.id])}"
			redirect action: 'search'
		}

		aclObjectIdentity
	}

	protected String lookupClassName() {
		'grails.plugin.springsecurity.acl.AclObjectIdentity'
	}

	protected Class<?> lookupClass() {
		grailsApplication.getDomainClass(lookupClassName()).clazz
	}

	protected Class<?> lookupAclClassClass() {
		grailsApplication.getDomainClass('grails.plugin.springsecurity.acl.AclClass').clazz
	}

	protected Class<?> lookupAclSidClass() {
		grailsApplication.getDomainClass('grails.plugin.springsecurity.acl.AclSid').clazz
	}
}
