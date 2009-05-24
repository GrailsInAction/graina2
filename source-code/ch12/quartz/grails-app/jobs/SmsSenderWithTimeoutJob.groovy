
class SmsSenderWithTimeoutJob {
    	def timeout = 10000 // execute job every 10 seconds
		def concurrent = false
		def volatility = false

    	def execute(context) {
    		log.debug 'Sending SMS Job at ${new Date()}'

			def failCounter = context.jobDetail.jobDataMap['failCounter'] ?: 0
			println "Failed Counter is ${failCounter}"
			try {
				// invoke service class to send SMS here, but we'll simulate fail
				throw new RuntimeException("SMS Website Timeout Simulation")
				failCounter = 0
			} catch (Exception te) {
				log.error "Failed invoking SMS Service"
				failCounter++
				if (failCounter > 5) {
					log.fatal "SMS has not left the building."
				}
			}
	   		context.jobDetail.jobDataMap['failCounter'] = failCounter
	   		log.debug "Finished SMS Job at ${new Date()}"
    	}
}

