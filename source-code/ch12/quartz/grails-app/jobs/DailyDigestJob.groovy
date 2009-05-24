
class DailyDigestJob {

    def cronExpression = "0 0 1 ? * MON-FRI"

    def dailyDigestService

    def execute() {
        log.debug "Starting the Daily Digest job."
        dailyDigestService.sendDailyDigests()
        log.debug "Finished the Daily Digest job."
    }

}
