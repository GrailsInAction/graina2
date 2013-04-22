package com.grailsinaction

class DateTagLib {
    static namespace = "hub"

    /**
     * Renders a date as a time from now, e.g. 10 days ago
     * or 3 mins ago.
     * @attr date REQUIRED The target date to render.
     */
    def dateFromNow = { attrs ->
        def date = attrs.date
        def niceDate = getNiceDate(date) // implement this somehow...
        out << niceDate
    }

    String getNiceDate(Date date) {
        final long second = 1000
        final long minute = second * 60
        final long hour = minute * 60
        final long day = hour * 24

        def now = new Date()
        def diff = Math.abs(now.time - date.time)
        def niceTime = ""
        long calc = 0
        calc = Math.floor(diff / day)
        if (calc) {
            niceTime += calc + " day" + (calc > 1 ? "s " : " ")
            diff %= day
        }
        calc = Math.floor(diff / hour)
        if (calc) {
            niceTime += calc + " hour" + (calc > 1 ? "s " : " ")
            diff %= hour
        }
        calc = Math.floor(diff / minute)
        if (calc) {
            niceTime += calc + " minute" + (calc > 1 ? "s " : " ")
            diff %= minute
        }
        if (!niceTime) {
            niceTime = "Right now"
        } else {
            niceTime += (date.time > now.time) ? "from now" : "ago"
        }
        return niceTime
    }

}
