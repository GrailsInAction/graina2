package com.grailsinaction

class SmsSenderWithTimeoutJob {

    def concurrent = false
    def volatility = false

    static triggers = {
      simple repeatInterval: 10000000 // execute job every 10 seconds
    }

    def execute(context) {
        log.error "Sending SMS Job at ${new Date()}"
        def failCounter = context.jobDetail.jobDataMap['failCounter'] ?: 0
        log.error "Failed Counter is ${failCounter}"
        try {
            // invoke service class to send SMS here & reset fail count
            throw new RuntimeException("Simulate SMS service failing")
        } catch (te) {
            failCounter++
            log.error "Failed invoking SMS Service. Fail count is ${failCounter}"
            if (failCounter == 5) {
                log.fatal "SMS has not left the building."
            }
        }
        context.jobDetail.jobDataMap['failCounter'] = failCounter
        log.error "Finished SMS Job at ${new Date()}"
    }
}
