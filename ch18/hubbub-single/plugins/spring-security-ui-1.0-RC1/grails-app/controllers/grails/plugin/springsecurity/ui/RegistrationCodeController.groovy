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
class RegistrationCodeController extends AbstractS2UiController {

	def edit() {
		def registrationCode = findById()
		if (!registrationCode) return

		[registrationCode: registrationCode]
	}

	def update() {
		def registrationCode = findById()
		if (!registrationCode) return
		if (!versionCheck('registrationCode.label', 'RegistrationCode', registrationCode, [registrationCode: registrationCode])) {
			return
		}

		if (!springSecurityUiService.updateRegistrationCode(registrationCode, params.username, params.token)) {
			render view: 'edit', model: [registrationCode: registrationCode]
			return
		}

		flash.message = "${message(code: 'default.updated.message', args: [message(code: 'registrationCode.label', default: 'RegistrationCode'), registrationCode.id])}"
		redirect action: 'edit', id: registrationCode.id
	}

	def delete() {
		def registrationCode = findById()
		if (!registrationCode) return

		try {
			springSecurityUiService.deleteRegistrationCode registrationCode
			flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'registrationCode.label', default: 'RegistrationCode'), params.id])}"
			redirect action: 'search'
		}
		catch (DataIntegrityViolationException e) {
			flash.error = "${message(code: 'default.not.deleted.message', args: [message(code: 'registrationCode.label', default: 'RegistrationCode'), params.id])}"
			redirect action: 'edit', id: params.id
		}
	}

	def search() {}

	def registrationCodeSearch() {

		boolean useOffset = params.containsKey('offset')
		setIfMissing 'max', 10, 100
		setIfMissing 'offset', 0

		def hql = new StringBuilder('FROM RegistrationCode rc WHERE 1=1 ')
		def queryParams = [:]

		if (params.token) {
			hql.append " AND LOWER(rc.token) LIKE :token"
			queryParams.token = params.token.toLowerCase() + '%'
		}

		if (params.username) {
			hql.append " AND LOWER(rc.username) LIKE :username"
			queryParams.username = params.username.toLowerCase() + '%'
		}

		int totalCount = RegistrationCode.executeQuery("SELECT COUNT(DISTINCT rc) $hql", queryParams)[0]

		Integer max = params.int('max')
		Integer offset = params.int('offset')

		String orderBy = ''
		if (params.sort) {
			orderBy = " ORDER BY rc.$params.sort ${params.order ?: 'ASC'}"
		}

		def results = RegistrationCode.executeQuery(
				"SELECT DISTINCT rc $hql $orderBy",
				queryParams, [max: max, offset: offset])
		def model = [results: results, totalCount: totalCount, searched: true]

		// add query params to model for paging
		for (name in ['username', 'token']) {
		 	model[name] = params[name]
		}

		render view: 'search', model: model
	}

	/**
	 * Ajax call used by autocomplete textfield.
	 */
	def ajaxRegistrationCodeSearch() {

		def jsonData = []

		if (params.term?.length() > 2) {
			String username = params.term

			setIfMissing 'max', 10, 100

			def results = RegistrationCode.executeQuery(
					"SELECT DISTINCT rc.username " +
					"FROM RegistrationCode rc " +
					"WHERE LOWER(rc.username) LIKE :name " +
					"ORDER BY rc.username ",
					[name: "%${username.toLowerCase()}%"],
					[max: params.max])

			for (result in results) {
				jsonData << [value: result]
			}
		}

		render text: jsonData as JSON, contentType: 'text/plain'
	}

	protected RegistrationCode findById() {
		def registrationCode = RegistrationCode.get(params.id)
		if (!registrationCode) {
			flash.error = "${message(code: 'default.not.found.message', args: [message(code: 'registrationCode.label', default: 'RegistrationCode'), params.id])}"
			redirect action: 'search'
		}

		registrationCode
	}
}
