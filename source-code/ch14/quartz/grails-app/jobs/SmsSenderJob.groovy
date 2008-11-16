
class SmsSenderJob {
	
    def timeout = 10000 // execute job every 10 seconds
	def concurrent = false

    def execute() {
       log.error "Sending SMS Job at ${new Date()}"
	   Thread.sleep(20000) // simulate web service delay of 20 secs
	   log.error "Finished SMS Job at ${new Date()}"
    }
}

