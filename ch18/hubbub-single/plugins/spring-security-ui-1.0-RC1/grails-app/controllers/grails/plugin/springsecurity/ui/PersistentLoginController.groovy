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
import grails.plugin.springsecurity.SpringSecurityUtils

import org.springframework.dao.DataIntegrityViolationException

/**
 * @author <a href='mailto:burt@burtbeckwith.com'>Burt Beckwith</a>
 */
class PersistentLoginController extends AbstractS2UiController {

	def edit() {
		def persistentLogin = findById()
		if (!persistentLogin) return

		[persistentLogin: persistentLogin]
	}

	def update() {
		def persistentLogin = findById()
		if (!persistentLogin) return
		if (!versionCheck('persistentLogin.label', 'PersistentLogin', persistentLogin, [persistentLogin: persistentLogin])) {
			return
		}

		if (!springSecurityUiService.updatePersistentLogin(persistentLogin, params)) {
			render view: 'edit', model: [persistentLogin: persistentLogin]
			return
		}

		flash.message = "${message(code: 'default.updated.message', args: [message(code: 'persistentLogin.label', default: 'PersistentLogin'), persistentLogin.id])}"
		redirect action: 'edit', id: persistentLogin.id
	}

	def delete() {
		def persistentLogin = findById()
		if (!persistentLogin) return

		try {
			springSecurityUiService.deletePersistentLogin persistentLogin
			flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'persistentLogin.label', default: 'PersistentLogin'), params.id])}"
			redirect action: 'search'
		}
		catch (DataIntegrityViolationException e) {
			flash.error = "${message(code: 'default.not.deleted.message', args: [message(code: 'persistentLogin.label', default: 'PersistentLogin'), params.id])}"
			redirect action: 'edit', id: params.id
		}
	}

	def search() {}

	def persistentLoginSearch() {

		boolean useOffset = params.containsKey('offset')
		setIfMissing 'max', 10, 100
		setIfMissing 'offset', 0

		def hql = new StringBuilder('FROM ').append(lookupPersistentLoginClassName()).append(' pl WHERE 1=1 ')
		def queryParams = [:]

		for (name in ['username', 'series', 'token']) {
			String param = params[name]
			if (param) {
				if (name == 'series') name = 'id' // aliased primary key
				queryParams[name] = '%' + param.toLowerCase() + '%'
				hql.append " AND LOWER(pl.$name) LIKE :$name"
			}
		}

		int totalCount = lookupPersistentLoginClass().executeQuery(
			"SELECT COUNT(DISTINCT pl) $hql", queryParams)[0]

		Integer max = params.int('max')
		Integer offset = params.int('offset')

		String orderBy = ''
		if (params.sort) {
			orderBy = " ORDER BY pl.$params.sort ${params.order ?: 'ASC'}"
		}

		def results = lookupPersistentLoginClass().executeQuery(
				"SELECT DISTINCT pl $hql $orderBy",
				queryParams, [max: max, offset: offset])
		def model = [results: results, totalCount: totalCount, searched: true]

		// add query params to model for paging
		for (name in ['username', 'series', 'token']) {
		 	model[name] = params[name]
		}

		render view: 'search', model: model
	}

	/**
	 * Ajax call used by autocomplete textfield.
	 */
	def ajaxPersistentLoginSearch() {

		def jsonData = []

		if (params.term?.length() > 2) {
			String username = params.term

			setIfMissing 'max', 10, 100

			def results = lookupPersistentLoginClass().executeQuery(
					"SELECT DISTINCT pl.username " +
					"FROM ${lookupPersistentLoginClassName()} pl " +
					"WHERE LOWER(pl.username) LIKE :name " +
					"ORDER BY pl.username",
					[name: "%${username.toLowerCase()}%"],
					[max: params.max])

			for (result in results) {
				jsonData << [value: result]
			}
		}

		render text: jsonData as JSON, contentType: 'text/plain'
	}

	protected findById() {
		def persistentLogin = lookupPersistentLoginClass().get(params.id)
		if (!persistentLogin) {
			flash.error = "${message(code: 'default.not.found.message', args: [message(code: 'persistentLogin.label', default: 'PersistentLogin'), params.id])}"
			redirect action: 'search'
		}

		persistentLogin
	}

	protected String lookupPersistentLoginClassName() {
		SpringSecurityUtils.securityConfig.rememberMe.persistentToken.domainClassName
	}

	protected Class<?> lookupPersistentLoginClass() {
		grailsApplication.getDomainClass(lookupPersistentLoginClassName()).clazz
	}
}
