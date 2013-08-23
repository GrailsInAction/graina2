package com.grailsinaction

import geb.spock.GebReportingSpec
import spock.lang.Stepwise

//@Stepwise
class TimelineFunctionalSpec extends GebReportingSpec {
    def "Does timeline load for user 'phil'"() {
        when:
        go "users/phil"

        then:
        $("#newPost h3").text() == "What is Phil Potts hacking on right now?"
    }

    /*
    def "Submitting a new post"() {
        given: "I start at phil's timeline page"
        go "users/phil?logonId=phil"

        when: "I enter a new message and post it"
        $("#postContent").value("This is a test post from Geb")
        $("#newPost").find("input", type: "button").click()

        then: "I see the new post in the timeline"
        waitFor { !$("div.postText", text: "This is a test post from Geb").empty }
    }
    */
}
