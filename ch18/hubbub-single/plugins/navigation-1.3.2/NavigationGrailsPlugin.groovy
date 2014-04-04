class NavigationGrailsPlugin {
    def version = '1.3.2'

    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.2 > *"

    def dependsOn = [controllers:"1.0 > *"]
    def observe = ['controllers']
    def loadAfter = ['logging']
    
    def author = "Marc Palmer"
    def authorEmail = "marc@grailsrocks.com"
    def title = "Site Menu Navigation"
    def description = '''\
Tags for doing site navigation and menus by convention
'''
    def documentation = "http://grails.org/Navigation+Plugin"

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }
   
    def doWithApplicationContext = { applicationContext ->
    }

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional)
    }
	                                      
    def doWithDynamicMethods = { ctx ->
        refreshNavigation(application, applicationContext)
    }
	
    def onChange = { event ->
        if (event.source instanceof Class) {
            refreshNavigation(application, applicationContext)
        }
    }

    private refreshNavigation(application, applicationContext) {
        def navSrv = applicationContext.navigationService
        navSrv.reset()

        application.controllerClasses.each { controllerClass ->
            // If there is a navigation property that is not null or false, we include it
            def nav = false
            if (controllerClass.clazz.metaClass.hasProperty(controllerClass.clazz, 'navigation')) {
                nav = controllerClass.clazz.navigation
            }
            if (nav) {
                navSrv.registerItem(controllerClass)
            }
        }

        navSrv.updated()        
    }
    
    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
}
