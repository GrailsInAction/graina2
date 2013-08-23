package com.grailsinaction

import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(DateTagLib)
class DateTagLibSpec extends Specification {

    @Unroll
    void "Conversion of #testName matches #expectedNiceDate"() {

        expect:
        applyTemplate('<hub:dateFromNow date="${date}" />', [date: testDate]) == expectedNiceDate

        where:
        testName       | testDate            | expectedNiceDate
        "Current Time" | new Date()          | "Right now"
        "Now - 1 day"  | new Date().minus(1) | "1 day ago"
        "Now - 2 days" | new Date().minus(2) | "2 days ago"
    }

}

