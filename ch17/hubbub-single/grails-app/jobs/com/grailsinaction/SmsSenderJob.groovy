package com.grailsinaction

class SmsSenderJob {

    def concurrent = false

    static triggers = {
      simple repeatInterval: 100000 // execute job every 100 seconds
    }

    def execute() {
        log.error "Sending SMS Job at ${new Date()}"
        Thread.sleep(20000)
        log.error "Finished SMS Job at ${new Date()}"

    }
}
