package com.grailsinaction

class SmsSenderJob {

    def concurrent = false

    static triggers = {
      simple repeatInterval: 10000000 // execute job every 10 seconds
    }

    def execute() {
        log.error "Sending SMS Job at ${new Date()}"
        Thread.sleep(20000)
        log.error "Finished SMS Job at ${new Date()}"

    }
}
