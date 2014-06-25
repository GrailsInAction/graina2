import com.grailsinaction.security.SecurityData

class GrainaSecurityGrailsPlugin {
    // the plugin version
    def version = "0.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.3 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]

    // TODO Fill in these fields
    def title = "Graina Security Plugin" // Headline display name of the plugin
    def author = "Grails in Action"
    def authorEmail = ""
    def description = '''\
A simple security plugin - do not use! Just for demonstration purposes.
'''

    def observe = ["controllers"]

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/graina-security"

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    def license = "APACHE"

    // Details of company behind the plugin (if there is one)
//    def organization = [ name: "My Company", url: "http://www.my-company.com/" ]

    // Any additional developers beyond the author specified above.
//    def developers = [ [ name: "Joe Bloggs", email: "joe@bloggs.net" ]]

    // Location of the plugin's issue tracker.
//    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPMYPLUGIN" ]

    // Online location of the plugin's browseable source code.
//    def scm = [ url: "http://svn.codehaus.org/grails-plugins/" ]

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before
    }

    def doWithSpring = {
        grainaSecurityData(SecurityData)
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

    def doWithApplicationContext = { ctx ->
        def securityData = ctx.getBean("grainaSecurityData")

        for (controllerClass in application.controllerClasses) {
            processController(controllerClass, securityData)
        }
    }

    def onChange = { event ->
        if (application.isControllerClass(event.source)) {
            def clsDesc = application.getControllerClass(event.source?.name)

            if (clasDesc == null) {
                clsDesc = application.addArtefact(
                        ControllerArtefactHandler.TYPE,
                        event.source)
            }

            def securityData = event.ctx.getBean("grainaSecurityData")
            processController(clsDesc, securityData)
        }
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    def onShutdown = { event ->
        // TODO Implement code that is executed when the application shuts down (optional)
    }

    private processController(clsDesc, securityData) {
        def controllerName = controllerClass.logicalPropertyName
        def actionMap = securityData.authRequiredActions
        if (actionMap[controllerName] == null) {
            actionMap[controllerName] = []
        }

        for (method in controllerClass.clazz.declaredMethods) {
            def ann = field.getAnnotation(AuthRequired)
            if (ann != null) {
                def actionName = method.name
                actionMap[controllerName] << actionName
            }
        }
    }
}
