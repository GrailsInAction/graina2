/* Copyright 2009-2012 SpringSource.
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
import grails.util.GrailsNameUtils
import groovy.text.SimpleTemplateEngine

includeTargets << grailsScript('_GrailsBootstrap')

USAGE = """
	Usage: grails s2ui-override <type> <controller-package>

	Copies plugin controllers and GSPs to the application so they can be overridden.

	<type> can be one of user, role, requestmap, securityinfo, aclsid, aclobjectidentity,
	aclentry, aclclass, persistentlogin, register, registrationcode, auth, or layout (not case-sensitive)

	<controller-package> is required for any type that has a controller (i.e. all but 'auth' and 'layout')

	Example: grails s2ui-override user com.yourcompany.yourapp
"""

overwriteAll = false
pluginViewsDir = "$springSecurityUiPluginDir/grails-app/views"
appGrailsApp = "$basedir/grails-app"

templateAttributes = [:]
templateDir = "$springSecurityUiPluginDir/src/templates"
templateEngine = new SimpleTemplateEngine()

controllers = [aclclass: 'AclClass',
               aclentry: 'AclEntry',
               aclobjectidentity: 'AclObjectIdentity',
               aclsid: 'AclSid',
               persistentlogin: 'PersistentLogin',
               register: 'Register',
               registrationcode: 'RegistrationCode',
               requestmap: 'Requestmap',
               role: 'Role',
               securityinfo: 'SecurityInfo',
               user: 'User']

target(s2uiOverride: 'Copy plugin UI files to the project so they can be overridden') {
	copyFiles()
}

private void copyFiles() {

	String[] typeAndPackage = parseArgs()
	if (!typeAndPackage) {
		return
	}
	if (typeAndPackage.length == 1) {
		if ('layout'.equals(typeAndPackage[0])) {
			// special case for springSecurityUI.gsp
			copyFile "$pluginViewsDir/layouts/springSecurityUI.gsp",
			         "$appGrailsApp/views/layouts/springSecurityUI.gsp"

			ant.mkdir dir: "$appGrailsApp/views/includes"
			copyFile "$pluginViewsDir/includes/_ajaxLogin.gsp",
				      "$appGrailsApp/views/includes/_ajaxLogin.gsp"
		}
		else if ('auth'.equals(typeAndPackage[0])) {
			// special case for auth.gsp
			ant.mkdir dir: "$appGrailsApp/views/login"
			copyFile "$pluginViewsDir/login/auth.gsp",
				      "$appGrailsApp/views/login/auth.gsp"
		}
		return
	}

	String type = typeAndPackage[0]
	String controller = controllers[type.toLowerCase()]
	if (!controller) {
		errorMessage "\nERROR: unknown type '$type'\n${USAGE}"
		return
	}

	printMessage "Copying $type resources"

	String packageName = typeAndPackage[1].trim()
	if ('grails.plugin.springsecurity.ui'.equals(packageName)) {
		errorMessage "\nERROR: The controller package cannot be the same as the plugin controller\n"
		return
	}

	// copy controller
	String dir = packageName.replaceAll('\\.', '/')
	ant.mkdir dir: "$appGrailsApp/controllers/$dir"

	templateAttributes.packageDeclaration = "package $packageName"
	generateFile "$templateDir/${controller}Controller.groovy.template",
	             "$appGrailsApp/controllers/$dir/${controller}Controller.groovy"

	// copy GSPs
	dir = GrailsNameUtils.getPropertyName(controller)
	ant.mkdir dir: "$appGrailsApp/views/$dir"
	for (gsp in new File(pluginViewsDir + '/' + dir).listFiles()) {
		if (gsp.name.toLowerCase().endsWith('.gsp')) {
			copyFile gsp.path, "$appGrailsApp/views/$dir/${gsp.name}"
		}
	}

	if ('register'.equalsIgnoreCase(type)) {
		copyFile "$pluginViewsDir/layouts/register.gsp",
		         "$appGrailsApp/views/layouts/register.gsp"
	}
}

private parseArgs() {
	args = args ? args.split('\n') : []
	if (args.size() == 2) {
		return args
	}
	if (args.size() == 1 && (args[0].equalsIgnoreCase('auth') || args[0].equalsIgnoreCase('layout'))) {
		return args
	}

	errorMessage USAGE
	null
}

okToWrite = { String dest ->

	def file = new File(dest)
	if (overwriteAll || !file.exists()) {
		return true
	}

	String propertyName = "file.overwrite.$file.name"
	ant.input(addProperty: propertyName, message: "$dest exists, ok to overwrite?",
	          validargs: 'y,n,a', defaultvalue: 'y')

	if (ant.antProject.properties."$propertyName" == 'n') {
		return false
	}

	if (ant.antProject.properties."$propertyName" == 'a') {
		overwriteAll = true
	}

	true
}

copyFile = { String from, String to ->
	if (!okToWrite(to)) {
		return
	}

	ant.copy file: from, tofile: to, overwrite: true
}

generateFile = { String templatePath, String outputPath ->
	if (!okToWrite(outputPath)) {
		return
	}

	File templateFile = new File(templatePath)
	if (!templateFile.exists()) {
		errorMessage "\nERROR: $templatePath doesn't exist"
		return
	}

	File outFile = new File(outputPath)

	ant.mkdir dir: outFile.parentFile

	outFile.withWriter { writer ->
		templateEngine.createTemplate(templateFile.text).make(templateAttributes).writeTo(writer)
	}

	printMessage "generated $outFile.absolutePath"
}

printMessage = { String message -> event('StatusUpdate', [message]) }
errorMessage = { String message -> event('StatusError', [message]) }

setDefaultTarget 's2uiOverride'
