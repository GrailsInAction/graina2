import com.grailsinaction.security.HashPasswordHandler
import com.grailsinaction.security.Sha1Hasher
import org.codehaus.groovy.grails.commons.GrailsClassUtils as GCU
import org.codehaus.groovy.grails.plugins.web.filters.FilterConfig

class SecurityGrailsPlugin {
    // the plugin version
    def version = "0.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.1 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [ "grails-app/views/error.gsp" ]

    def observe = [ "controllers", "filters" ]

    // TODO Fill in these fields
    def author = "Peter Ledbrook"
    def authorEmail = ""
    def title = "Simple security plugin"
    def description = '''\\
Provides basic access control based on roles.
'''

    private hasRoleImpl = { name ->
        def u = session.user
        if (u) {
            u.merge()
            return u.roles.any { it.name == name }
        }
        else {
            return false
        }
    }

    // URL to the plugin's documentation
    def documentation = "http://grails.org/Security+Plugin"

    def doWithSpring = {
        // Default hashing algorithm for password storage.
        hashMachine(Sha1Hasher)

        passwordHandler(HashPasswordHandler) {
            hasher = ref("hashMachine")
            iterationCount = 5
        }
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional)
    }

    def doWithDynamicMethods = { ctx ->
        // Add a hasRole() method to secured controllers.
        application.controllerClasses.each { cClass ->
            def isSecure = GCU.getStaticPropertyValue(
                    cClass.clazz,
                    "secure")

            if (isSecure) {
                cClass.metaClass.hasRole = hasRoleImpl.clone()
            }
        }

        // Add authcRequired() and roleRequired() methods to filters.
        FilterConfig.metaClass.authcRequired = {->
            if (session.user == null) redirect(controller: "login", action: "index")
            return session.user != null
        }
        FilterConfig.metaClass.roleRequired = hasRoleImpl.clone()
    }

    def onChange = { event ->
        if (application.isControllerClass(event.source)) {
            def isSecure = GCU.getStaticPropertyValue(
                    event.source,
                    "secure")
            if (isSecure) {
                event.source.metaClass.hasRole = hasRoleImpl.clone()
            }
        }
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
}
