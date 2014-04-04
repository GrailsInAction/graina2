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

import grails.converters.JSON

import org.springframework.dao.DataIntegrityViolationException

/**
 * @author <a href='mailto:burt@burtbeckwith.com'>Burt Beckwith</a>
 */
class AclSidController extends AbstractS2UiController {

	def create() {
		[aclSid: lookupClass().newInstance(params)]
	}

	def save() {
		def aclSid = lookupClass().newInstance(params)
		if (!aclSid.save(flush: true)) {
			render view: 'create', model: [aclSid: aclSid]
			return
		}

		flash.message = "${message(code: 'default.created.message', args: [message(code: 'aclSid.label', default: 'AclSid'), aclSid.id])}"
		redirect action: 'edit', id: aclSid.id
	}

	def edit() {
		def aclSid = findById()
		if (!aclSid) return

		[aclSid: aclSid]
	}

	def update() {
		def aclSid = findById()
		if (!aclSid) return
		if (!versionCheck('aclSid.label', 'AclSid', aclSid, [aclSid: aclSid])) {
			return
		}

		if (!springSecurityUiService.updateAclSid(aclSid, params.sid, params.principal == 'on')) {
			render view: 'edit', model: [aclSid: aclSid]
			return
		}

		flash.message = "${message(code: 'default.updated.message', args: [message(code: 'aclSid.label', default: 'AclSid'), aclSid.id])}"
		redirect action: 'edit', id: aclSid.id
	}

	def delete() {
		def aclSid = findById()
		if (!aclSid) return

		try {
			springSecurityUiService.deleteAclSid aclSid
			flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'aclSid.label', default: 'AclSid'), params.id])}"
			redirect action: 'search'
		}
		catch (DataIntegrityViolationException e) {
			flash.error = "${message(code: 'default.not.deleted.message', args: [message(code: 'aclSid.label', default: 'AclSid'), params.id])}"
			redirect action: 'edit', id: params.id
		}
	}

	def search() {
		[principal: 0]
	}

	def aclSidSearch() {

		boolean useOffset = params.containsKey('offset')
		setIfMissing 'max', 10, 100
		setIfMissing 'offset', 0

		def hql = new StringBuilder('FROM ').append(lookupClassName()).append(' s WHERE 1=1 ')
		def queryParams = [:]

		for (name in ['sid']) {
			if (params[name]) {
				hql.append " AND LOWER(s.$name) LIKE :$name"
				queryParams[name] = params[name].toLowerCase() + '%'
			}
		}

		for (name in ['principal']) {
			Integer value = params.int(name)
			if (value) {
				hql.append " AND s.$name=:$name"
				queryParams[name] = value == 1
			}
		}

		int totalCount = lookupClass().executeQuery("SELECT COUNT(DISTINCT s) $hql", queryParams)[0]

		Integer max = params.int('max')
		Integer offset = params.int('offset')

		String orderBy = ''
		if (params.sort) {
			orderBy = " ORDER BY s.$params.sort ${params.order ?: 'ASC'}"
		}

		def results = lookupClass().executeQuery(
				"SELECT DISTINCT s $hql $orderBy",
				queryParams, [max: max, offset: offset])
		def model = [results: results, totalCount: totalCount, searched: true]

		// add query params to model for paging
		for (name in ['sid', 'principal']) {
		 	model[name] = params[name]
		}

		render view: 'search', model: model
	}

	/**
	 * Ajax call used by autocomplete textfield.
	 */
	def ajaxAclSidSearch() {

		def jsonData = []

		if (params.term?.length() > 2) {
			String sid = params.term
			setIfMissing 'max', 10, 100

			def results = lookupClass().executeQuery(
					"SELECT DISTINCT s.sid " +
					"FROM ${lookupClassName()} s " +
					"WHERE LOWER(s.sid) LIKE :name " +
					"ORDER BY s.sid ",
					[name: "%${sid.toLowerCase()}%"],
					[max: params.max])
			for (result in results) {
				jsonData << [value: result]
			}
		}

		render text: jsonData as JSON, contentType: 'text/plain'
	}

	protected findById() {
		def aclSid = lookupClass().get(params.id)
		if (!aclSid) {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'aclSid.label', default: 'AclSid'), params.id])}"
			redirect action: 'search'
		}

		aclSid
	}

	protected String lookupClassName() {
		'grails.plugin.springsecurity.acl.AclSid'
	}

	protected Class<?> lookupClass() {
		grailsApplication.getDomainClass(lookupClassName()).clazz
	}
}
