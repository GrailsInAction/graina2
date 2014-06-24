package com.grailsinaction

class DailyDigestJob {

    def dailyDigestService

    def concurrent = false

    static triggers = {
      cron cronExpression: "0 0 1 ? * MON-FRI"
    }

    def execute() {
        log.debug "Starting the Daily Digest job."
        dailyDigestService.sendDailyDigests()
        Thread.sleep 3000
        log.debug "Finished the Daily Digest job."
    }

}
