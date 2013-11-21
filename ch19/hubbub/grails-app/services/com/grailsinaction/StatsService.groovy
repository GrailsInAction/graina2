package com.grailsinaction

class StatsService {

    static transactional = false

    def redisService

    def mongo

    def countAuditMessageByUser() {

        def db = mongo.getDB("audit")
        def result = db.logs.mapReduce("""
    function map() {
        emit(this.userId, this.message)
    }
""",
                """
    function reduce(userId, auditMessages) {
        return auditMessages.length
    }
""",
                "auditCount", [:]
        )

        def countMap = [ : ]

        db.auditCount.find().each { counter ->
            countMap[counter._id] = counter.value
        }

        return countMap
    }

    def nativeMongoCollectionQuery() {

        def entries = AuditEntry.collection.find(tags: [ name: 'post' ])

        if (entries) {

            StringBuilder sb = new StringBuilder("<html><ul>\n")
            entries.each { entry ->
                AuditEntry ae = entry as AuditEntry
                sb.append("<li>${ae.dump()}</li>\n")
            }
            sb.append("</ul></html>\n")

            log.debug "${sb.toString()}"

        } else {

            log.warn "Audit Entries is Empty"
        }

        return entries
    }

    @grails.events.Listener
    void onNewPost(Post newPost) {

        String dateToday = new Date().format("yy-MM-dd")
        String redisTotalsKey = "daily.stat.totalPosts.${dateToday}"

        log.debug "New Post from: ${newPost.user.loginId}"

        redisService.incr(redisTotalsKey)

        log.debug "Total Posts at: ${redisService.get(redisTotalsKey)}"

        String redisTotalsByUserKey = "daily.stat.totalsByUser.${dateToday}"

        log.debug "Keying off ${redisTotalsByUserKey} for ${newPost.user.loginId}"
        redisService.zincrby(redisTotalsByUserKey, 1, newPost.user.loginId)
        int usersPostsToday = redisService.zscore(redisTotalsByUserKey, newPost.user.loginId)
        log.debug "Incremented daily stat for ${newPost.user.loginId} to ${usersPostsToday}"

    }

    def getTodaysTopPosters() {
        String dateToday = new Date().format("yy-MM-dd")
        String redisTotalsByUserKey = "daily.stat.totalsByUser.${dateToday}"
        def tuples = redisService.zrevrangeWithScores(redisTotalsByUserKey, 0, 1000)
        tuples.each { tuple ->
            log.info("Posts for ${tuple.element} -> ${tuple.score}")
        }
        return tuples
    }


}
