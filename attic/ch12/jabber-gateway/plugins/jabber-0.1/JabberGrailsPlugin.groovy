import org.codehaus.groovy.grails.commons.GrailsClassUtils
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CFG

import org.codehaus.groovy.grails.jabber.ChatListener


class JabberGrailsPlugin {
    def version = 0.1
    def dependsOn = [:]

    def author = "Glen Smith"
    def authorEmail = "glen@bytecode.com.au"
    def title = "Jabber Plugin"
    def description = '''\
This plugin provides the opportunity to send and receive Chat messages via the Jabber API.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/Jabber+Plugin"

    def doWithSpring = {

        application.serviceClasses?.each { service ->
			def serviceClass = service.getClazz()
			def exposeList = GrailsClassUtils.getStaticPropertyValue(serviceClass, 'expose')
			if (exposeList != null && exposeList.contains('jabber')) {
				println "adding Jabber listener for ${service.shortName} to Spring"

				def method = GrailsClassUtils.getStaticPropertyValue(serviceClass, 'jabberListenerMethod')
				if (!method)
					method = "onJabberMessage"

				"${service.propertyName}JabberListener"(org.codehaus.groovy.grails.jabber.ChatListener) {
                    host = CFG.config.chat.host
                    port = CFG.config.chat.port
                    serviceName = CFG.config.chat.serviceName
                    userName = CFG.config.chat.username
                    password = CFG.config.chat.password
                    listenerMethod = method
                    targetService = ref("${service.propertyName}")

                }

				
			}
		}

    }
   
    def doWithApplicationContext = { applicationContext ->

        application.serviceClasses?.each { service ->
		def serviceClass = service.getClazz()
		def exposeList = GrailsClassUtils.getStaticPropertyValue(serviceClass, 'expose')
		if (exposeList!=null && exposeList.contains('jabber')) {
				println "Starting Jabber listener for ${service.shortName}"
                def listener = applicationContext.getBean("${service.propertyName}JabberListener")
                listener.listen()
                println "Added listener ok ${listener.dump()}"
            }
		}


    }

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional)
    }

    def sendJabberMessage = {cl, to, message ->
    	
    	if (to instanceof List) {
    	    to.each { addr ->
                cl.sendJabberMessage(addr, message)    
            }
        } else {
            cl.sendJabberMessage(to, message)
        }

	}
	                                      
    def doWithDynamicMethods = { ctx ->

        ChatListener cl = new ChatListener(host: CFG.config.chat.host,
                                serviceName: CFG.config.chat.serviceName,
                                userName: CFG.config.chat.username,
                                password: CFG.config.chat.password
                           )


        application.serviceClasses?.each { service ->
            def mc = service.metaClass
            def listener = applicationContext.getBean("${service.propertyName}JabberListener")
            if (listener) {
                mc.sendJabberMessage = sendJabberMessage.curry(listener)
            } else {
			    mc.sendJabberMessage = sendJabberMessage.curry(cl)
            }
        }
		application.controllerClasses?.each { controller ->
    	   def mc = controller.metaClass
		   mc.sendJabberMessage = sendJabberMessage.curry(cl)
    	}

    }
	
    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
}
