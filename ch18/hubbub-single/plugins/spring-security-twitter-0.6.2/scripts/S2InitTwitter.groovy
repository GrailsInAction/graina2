import grails.util.GrailsNameUtils
import groovy.text.SimpleTemplateEngine

includeTargets << grailsScript("Init")
includeTargets << grailsScript('_GrailsBootstrap')

overwriteAll = false
templateAttributes = [:]
templateDir = "$springSecurityTwitterPluginDir/src/templates"
resourceDir = "$springSecurityTwitterPluginDir/src/resources"
pluginAppDir = "$springSecurityTwitterPluginDir/grails-app"
appDir = "$basedir/grails-app"
webDir = "$basedir/web-app"
templateEngine = new SimpleTemplateEngine()
pluginConfig = [:]

target(s2InitTwitter: 'Initializes Twitter artifacts for the Spring Security Twitter plugin') {
	depends(checkVersion, configureProxy, packageApp, classpath)

    def configFile = new File("$springSecurityTwitterPluginDir/grails-app/conf/DefaultTwitterSecurityConfig.groovy")
	if (configFile.exists()) {
        def conf = new ConfigSlurper().parse(configFile.text)
        println "Creating app based on configuration:"
        pluginConfig.each { name, config ->
		    println "$name = ${config.flatten()}"
        }

        pluginConfig = conf.security.twitter
        pluginConfig.each { name, config ->
		    println "$name = $config"
        }
	} else {
        ant.echo message: "ERROR $configFile.path not found"
    }

	configure()
	copyData()
    fillConfig()

    ant.echo message: "**********************************************************************************************  "
    ant.echo message: "Please add following line into your layout's head:"
    ant.echo message: "  "
    ant.echo message: '  <link rel="stylesheet" href="${resource(dir:\'css\',file:\'twitter-auth.css\')}" />'
    ant.echo message: "  "
    ant.echo message: "How to use:"
    ant.echo message: "  put <twitterAuth:button/> tag into your GSP, where you want to sign in user with twitter"
    ant.echo message: "**********************************************************************************************  "
}

private void fillConfig() {
    def config = [:]
    String code

/*
    code = "twitter.appId"
    ant.input(message: "Enter your Twitter appId", addproperty: code)
    config['appId'] = ant.antProject.properties[code]
*/

    code = "twitter.app.key"
    ant.input(message: "Enter your Twitter API Key", addproperty: code)
    config['key'] = ant.antProject.properties[code]

    code = "twitter.app.consumerKey"
    ant.input(message: "Enter your Twitter API Consumer Key", addproperty: code, defaultvalue: config['app.key'])
    config['consumerKey'] = ant.antProject.properties[code]

    code = "twitter.app.consumerSecret"
    ant.input(message: "Enter your Twitter API Consumer Secret", addproperty: code)
    config['consumerSecret'] = ant.antProject.properties[code]

    def configFile = new File(appDir, 'conf/Config.groovy')
    if (configFile.exists()) {
        configFile.withWriterAppend {
            it.writeLine "\n"
            config.entrySet().each { Map.Entry conf ->
                it.writeLine "grails.plugin.springsecurity.twitter.$conf.key='$conf.value'"
            }
        }
    }
}

private void configure() {

	def SpringSecurityUtils = classLoader.loadClass('grails.plugin.springsecurity.SpringSecurityUtils')
	def conf = SpringSecurityUtils.securityConfig

	String userClassFullName = conf.userLookup.userDomainClassName
	checkValue userClassFullName, 'domain.userClassName'

	String userPackageName
	String userClassName
	(userPackageName, userClassName) = splitClassName(userClassFullName)

	String userConnectionProperty = pluginConfig.domain.connectionPropertyName
	checkValue userConnectionProperty, 'twitter.domain.connectionPropertyName'

    String domainClassName = pluginConfig.domain.classname
    checkValue domainClassName, 'twitter.domain.classname'

    String userImport = "import $userClassFullName"

	templateAttributes = [userClassFullName: userClassFullName,
	                      userClassName: userClassName,
                          domainClassName: domainClassName,
                          userConnectionProperty: userConnectionProperty,
                          userImport: userImport]

    templateAttributes.entrySet().each {
        println "$it.key = $it.value"
    }
}

private void copyData() {
	copyFile "$templateDir/spring-security-twitter.messages.properties.template",
		"$appDir/i18n/spring-security-twitter.messages.properties"
    copyFile "$springSecurityTwitterPluginDir/web-app/images/sign-in-with-twitter-d.png",
        "$webDir/images/sign-in-with-twitter-d.png"
    copyFile "$springSecurityTwitterPluginDir/web-app/css/twitter-auth.css",
        "$webDir/css/twitter-auth.css"

	generateFile "$templateDir/TwitterUser.groovy.connected.template",
	             "$appDir/domain/${templateAttributes.domainClassName}.groovy"
    generateFile "$templateDir/TwitterAuthController.groovy.template",
                 "$appDir/controllers/TwitterAuthController.groovy"

    ant.mkdir(dir:"$appDir/views/twitterAuth")
    ant.copy(todir: "$appDir/views/twitterAuth") {
        fileset dir:"$pluginAppDir/views/twitterAuth"
    }
}

packageToDir = { String packageName ->
	String dir = ''
	if (packageName) {
		dir = packageName.replaceAll('\\.', '/') + '/'
	}

	return dir
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

generateFile = { String templatePath, String outputPath ->
	if (!okToWrite(outputPath)) {
		return
	}

	File templateFile = new File(templatePath)
	if (!templateFile.exists()) {
		ant.echo message: "\nERROR: $templatePath doesn't exist"
		return
	}

	File outFile = new File(outputPath)

	// in case it's in a package, create dirs
	ant.mkdir dir: outFile.parentFile

	outFile.withWriter { writer ->
		templateEngine.createTemplate(templateFile.text).make(templateAttributes).writeTo(writer)
	}

	ant.echo message: "generated $outFile.absolutePath"
}

splitClassName = { String fullName ->

	int index = fullName.lastIndexOf('.')
	String packageName = ''
	String className = ''
	if (index > -1) {
		packageName = fullName[0..index-1]
		className = fullName[index+1..-1]
	}
	else {
		packageName = ''
		className = fullName
	}

	[packageName, className]
}

checkValue = { String value, String attributeName ->
	if (value == null || value.length() == 0 || value == '{}') {
		ant.echo message: "\nERROR: Cannot generate; $attributeName set as $value"
		System.exit 1
	}
}

copyFile = { String from, String to ->
	if (!okToWrite(to)) {
		return
	}

	ant.copy file: from, tofile: to, overwrite: true
}

setDefaultTarget 's2InitTwitter'
