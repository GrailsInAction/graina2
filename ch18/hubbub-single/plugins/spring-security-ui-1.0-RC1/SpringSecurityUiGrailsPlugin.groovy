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
import grails.plugin.springsecurity.SpringSecurityUtils

/**
 * @author <a href='mailto:burt@burtbeckwith.com'>Burt Beckwith</a>
 */
class SpringSecurityUiGrailsPlugin {

	String version = '1.0-RC1'
	String grailsVersion = '2.0.0 > *'
	List loadAfter = ['springSecurityCore']
	List pluginExcludes = [
		'docs/**',
		'src/docs/**',
		'scripts/CreateS2UiTestApps.groovy',
		'scripts/Fixpdf.groovy',
		'lib/**'
	]

	String author = 'Burt Beckwith'
	String authorEmail = 'burt@burtbeckwith.com'
	String title = 'Spring Security UI'
	String description = 'User interface extensions for the Spring Security plugin'
	String documentation = 'http://grails-plugins.github.io/grails-spring-security-ui/'

	String license = 'APACHE'
	def organization = [name: 'SpringSource', url: 'http://www.springsource.org/']
	def issueManagement = [system: 'JIRA', url: 'http://jira.grails.org/browse/GPSPRINGSECURITYUI']
	def scm = [url: 'https://github.com/grails-plugins/grails-spring-security-ui/']

	def doWithSpring = {

		def conf = SpringSecurityUtils.securityConfig
		if (!conf || !conf.active) {
			return
		}

		println '\nConfiguring Spring Security UI ...'

		SpringSecurityUtils.loadSecondaryConfig 'DefaultUiSecurityConfig'

		println '... finished configuring Spring Security UI\n'
	}
}
