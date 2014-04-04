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

/**
 * @author <a href='mailto:burt@burtbeckwith.com'>Burt Beckwith</a>
 */
abstract class AbstractS2UiController {

	static allowedMethods = [save: 'POST', update: 'POST', delete: 'POST']
	static defaultAction = 'search'

	def springSecurityService
	def springSecurityUiService

	protected boolean versionCheck(String messageCode, String messageCodeDefault, instance, model) {
		if (params.version) {
			def version = params.version.toLong()
			if (instance.version > version) {
				instance.errors.rejectValue('version', 'default.optimistic.locking.failure',
						[message(code: messageCode, default: messageCodeDefault)] as Object[],
						"Another user has updated this instance while you were editing")
				render view: 'edit', model: model
				return false
			}
		}
		true
	}

	protected void setIfMissing(String paramName, long valueIfMissing, Long max = null) {
		long value = (params[paramName] ?: valueIfMissing).toLong()
		if (max) {
			value = Math.min(value, max)
		}
		params[paramName] = value
	}

	protected String lookupUserClassName() {
		SpringSecurityUtils.securityConfig.userLookup.userDomainClassName
	}

	protected Class<?> lookupUserClass() {
		grailsApplication.getDomainClass(lookupUserClassName()).clazz
	}

	protected String lookupRoleClassName() {
		SpringSecurityUtils.securityConfig.authority.className
	}

	protected Class<?> lookupRoleClass() {
		grailsApplication.getDomainClass(lookupRoleClassName()).clazz
	}

	protected String lookupUserRoleClassName() {
		SpringSecurityUtils.securityConfig.userLookup.authorityJoinClassName
	}

	protected Class<?> lookupUserRoleClass() {
		grailsApplication.getDomainClass(lookupUserRoleClassName()).clazz
	}

	protected String lookupRequestmapClassName() {
		SpringSecurityUtils.securityConfig.requestMap.className
	}

	protected Class<?> lookupRequestmapClass() {
		grailsApplication.getDomainClass(lookupRequestmapClassName()).clazz
	}
}
