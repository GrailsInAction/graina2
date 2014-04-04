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

import grails.plugin.springsecurity.SpringSecurityUtils

import org.springframework.dao.DataIntegrityViolationException

/**
 * @author <a href='mailto:burt@burtbeckwith.com'>Burt Beckwith</a>
 */
class RequestmapController extends AbstractS2UiController {

	def search() {}

	def requestmapSearch() {

		boolean useOffset = params.containsKey('offset')
		setIfMissing 'max', 10, 100
		setIfMissing 'offset', 0

		def hql = new StringBuilder('FROM ').append(lookupRequestmapClassName()).append(' r WHERE 1=1 ')
		def queryParams = [:]

		String urlField = SpringSecurityUtils.securityConfig.requestMap.urlField
		String configAttributeField = SpringSecurityUtils.securityConfig.requestMap.configAttributeField

		for (name in [url: urlField, configAttribute: configAttributeField]) {
			if (params[name.key]) {
				hql.append " AND LOWER(r.${name.value}) LIKE :$name.key"
				queryParams[name.key] = '%' + params[name.key].toLowerCase() + '%'
			}
		}

		int totalCount = lookupUserClass().executeQuery("SELECT COUNT(DISTINCT r) $hql", queryParams)[0]

		Integer max = params.int('max')
		Integer offset = params.int('offset')

		String orderBy = ''
		if (params.sort) {
			orderBy = " ORDER BY r.$params.sort ${params.order ?: 'ASC'}"
		}

		def results = lookupRequestmapClass().executeQuery(
				"SELECT DISTINCT r $hql $orderBy",
				queryParams, [max: max, offset: offset])
		def model = [results: results, totalCount: totalCount, searched: true]

		// add query params to model for paging
		for (name in ['url', 'configAttribute', 'sort', 'order']) {
			model[name] = params[name]
		}

		render view: 'search', model: model
	}

	def create() {
		[requestmap: lookupRequestmapClass().newInstance(params)]
	}

	def save() {
		def requestmap = lookupRequestmapClass().newInstance(params)
		if (!requestmap.save(flush: true)) {
         render view: 'create', model: [requestmap: requestmap]
         return
		}

		springSecurityService.clearCachedRequestmaps()
		flash.error = "${message(code: 'default.created.message', args: [message(code: 'requestmap.label', default: 'Requestmap'), requestmap.id])}"
		redirect action: 'edit', id: requestmap.id
	}

	def edit() {
		def requestmap = findById()
		if (!requestmap) return

		[requestmap: requestmap]
	}

	def update() {
		def requestmap = lookupRequestmapClass().get(params.id)
		requestmap.properties = params

		if (!requestmap.save(flush: true)) {
			render view: 'edit', model: [requestmap: requestmap]
			return
		}

		springSecurityService.clearCachedRequestmaps()
		flash.message = "${message(code: 'default.updated.message', args: [message(code: 'requestmap.label', default: 'Requestmap'), requestmap.id])}"
		redirect action: 'edit', id: requestmap.id
	}

	def delete() {
		def requestmap = findById()
		if (!requestmap) return

		try {
			requestmap.delete(flush: true)
			springSecurityService.clearCachedRequestmaps()
			flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'requestmap.label', default: 'Requestmap'), params.id])}"
			redirect action: 'search'
		}
		catch (DataIntegrityViolationException e) {
			flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'requestmap.label', default: 'Requestmap'), params.id])}"
			redirect action: 'edit', id: params.id
		}
	}

	protected findById() {
		def requestmap = lookupRequestmapClass().get(params.id)
		if (!requestmap) {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'requestmap.label', default: 'Requestmap'), params.id])}"
			redirect action: 'search'
		}

		requestmap
	}
}
