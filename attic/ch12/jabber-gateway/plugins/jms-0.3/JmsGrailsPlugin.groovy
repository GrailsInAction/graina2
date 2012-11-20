import org.codehaus.groovy.grails.commons.GrailsClassUtils
import grails.util.GrailsUtil
import org.springframework.jms.core.JmsTemplate

class JmsGrailsPlugin {
	def version = 0.3
    def author = "Justin Edelson"
    def authorEmail = "justin@justinedelson.com"
    def title = "This plugin adds MDB functionality to services."
    
    def loadAfter = ['services', 'domainClass']
	def observe = ['services', 'controllers']
    def dependsOn = [services: GrailsUtil.getGrailsVersion(),
                     domainClass: GrailsUtil.getGrailsVersion()]
	
	def doWithSpring = {
		application.serviceClasses?.each { service ->
			def serviceClass = service.getClazz()
			def exposeList = GrailsClassUtils.getStaticPropertyValue(serviceClass, 'expose')
			if (exposeList != null && exposeList.contains('jms')) {
				println ">>>> adding JMS listener for ${service.shortName} to Spring"
				def sName = service.propertyName.replaceFirst("Service","")
					
				def listenerCount = GrailsClassUtils.getStaticPropertyValue(serviceClass, 'listenerCount')
				if (!listenerCount)
					listenerCount = 1
					
				def destination = GrailsClassUtils.getStaticPropertyValue(serviceClass, 'destination')
				if (!destination)
					destination = sName
					
				def selector = GrailsClassUtils.getStaticPropertyValue(serviceClass, 'messageSelector')
					
				def listenerMethod = GrailsClassUtils.getStaticPropertyValue(serviceClass, 'listenerMethod')
				if (!listenerMethod)
					listenerMethod = "onMessage"
						
				def pubSub = GrailsClassUtils.getStaticPropertyValue(serviceClass, 'pubSub')
				if (!pubSub)
					pubSub = false
					
				def durable = GrailsClassUtils.getStaticPropertyValue(serviceClass, 'durable')
				if (!durable)
					durable = false
					
				def id = GrailsClassUtils.getStaticPropertyValue(serviceClass, 'clientId')
				if (!id) {
					id = GrailsClassUtils.getStaticPropertyValue(serviceClass, 'id')
					if (!id) {
						id = "grailsAppListener"
					}
				}
				
				"${sName}JMSListener"(org.codehaus.grails.jms.ClosureMessageListenerAdapter, ref("${service.propertyName}")) {
					defaultListenerMethod = listenerMethod
				}
					
				"${sName}JMSListenerContainer"(org.springframework.jms.listener.DefaultMessageListenerContainer) {
					autoStartup = false
					concurrentConsumers = listenerCount
					destinationName = destination
					pubSubDomain = pubSub
					
					if (pubSub && durable) {
						subscriptionDurable = durable
						durableSubscriptionName = id
						clientId = id						
					}
					
					if (selector) {
					    messageSelector = selector
					}
					connectionFactory = ref("connectionFactory")
					messageListener = ref("${sName}JMSListener")
				}
			}
		}
	}   
	def doWithApplicationContext = { applicationContext ->
		application.serviceClasses?.each { service ->
		def serviceClass = service.getClazz()
		def exposeList = GrailsClassUtils.getStaticPropertyValue(serviceClass, 'expose')
		if (exposeList!=null && exposeList.contains('jms')) {
				println ">>>> starting JMS listener for ${service.shortName}"
				def sName = service.propertyName.replaceFirst("Service","")
				applicationContext.getBean("${sName}JMSListenerContainer").start()
			}
		}		
	}
	def doWithWebDescriptor = { xml ->
		// TODO Implement additions to web.xml (optional)
	}
    def sendJMSMessage = {jmsTemplate, destinationName, payload ->
    	def convertedPayload = payload
    	if (payload instanceof GString) {
    	    convertedPayload = payload.toString()
    	}
    	jmsTemplate.convertAndSend(destinationName, convertedPayload)
	}        
	def doWithDynamicMethods = { ctx ->
		def connectionFactory = ctx.getBean("connectionFactory")
		def queueTemplate = new JmsTemplate(connectionFactory)
		def topicTemplate = new JmsTemplate(connectionFactory)
		topicTemplate.setPubSubDomain(true)
		application.serviceClasses?.each { service ->
			def mc = service.metaClass
			mc.sendJMSMessage = sendJMSMessage.curry(queueTemplate)
			mc.sendQueueJMSMessage = sendJMSMessage.curry(queueTemplate)
			mc.sendPubSubJMSMessage = sendJMSMessage.curry(topicTemplate)
			mc.sendTopicJMSMessage = sendJMSMessage.curry(topicTemplate)
		}
		application.controllerClasses?.each { controller ->
    	   def mc = controller.metaClass
			mc.sendJMSMessage = sendJMSMessage.curry(queueTemplate)
			mc.sendQueueJMSMessage = sendJMSMessage.curry(queueTemplate)
			mc.sendPubSubJMSMessage = sendJMSMessage.curry(topicTemplate)
			mc.sendTopicJMSMessage = sendJMSMessage.curry(topicTemplate)
    	}
	}	
	def onChange = { event ->
		if (event.source && event.ctx) {
			/*def serviceClass = event.source.getClass()
			def exposeList = GrailsClassUtils.getStaticPropertyValue(serviceClass, 'expose')
			if (exposeList != null && exposeList.contains('jms')) {
				def sName = event.source.propertyName.replaceFirst("Service","")
				println ">>>> resetting delegate of JMS listener for ${event.source.shortName}"
				event.ctx.getBean("${sName}JMSListener").delegate = event.source
			}*/
			
		    def connectionFactory = event.ctx.getBean("connectionFactory")
			def queueTemplate = new JmsTemplate(connectionFactory)
			def topicTemplate = new JmsTemplate(connectionFactory)
		    topicTemplate.setPubSubDomain(true)
		    def mc = event.source.metaClass
			mc.sendJMSMessage = sendJMSMessage.curry(queueTemplate)
			mc.sendQueueJMSMessage = sendJMSMessage.curry(queueTemplate)
			mc.sendPubSubJMSMessage = sendJMSMessage.curry(topicTemplate)
			mc.sendTopicJMSMessage = sendJMSMessage.curry(topicTemplate)
		}
	}                                                                                  
	def onApplicationChange = { event ->
		// TODO Implement code that is executed when any class in a GrailsApplication changes
		// the event contain: event.source, event.application and event.applicationContext objects
	}
}
