package com.grailsinaction

class ControllableJob {

    def concurrent = false

    def group = "myServices"

    static triggers = {
      simple repeatInterval: 500000 // execute job every 5 seconds
    }

    def execute(context) {
        log.error "Controllable job running.. ${context.jobDetail.key.dump()}"
    }
}
