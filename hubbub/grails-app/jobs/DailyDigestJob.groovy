
class DailyDigestJob {

    def timeout = 24 * 60 * 60 * 1000
    def startDelay = 60 * 1000
    // def cronExpression = "0 0 1 ? * MON-FRI"

    def dailyDigestService

    def execute() {
        log.debug "Starting the Daily Digest job."
        // dailyDigestService.sendDailyDigests()
        log.debug "Finished the Daily Digest job."
    }

}
