package com.grailsinaction

import geb.spock.GebReportingSpec
import spock.lang.Stepwise

class TimelineFunctionalSpec extends GebReportingSpec {
    def "Does timeline load for user 'phil'"() {
        when:
        login "frankie", "testing"
        go "users/phil"

        then:
        $("#newPost h3").text() == "What is Phil Potts hacking on right now?"
    }

    def "Submitting a new post"() {
        given: "I log in and start at my timeline page"
        login "frankie", "testing"
        go "timeline"

        when: "I enter a new message and post it"
        $("#postContent").value("This is a test post from Geb")
        $("#newPost").find("input", type: "button").click()

        then: "I see the new post in the timeline"
        waitFor { !$("div.postText", text: "This is a test post from Geb").empty }
    }

    private login(String username, String password) {
        go "login/form"
        $("input[name='j_username']").value(username)
        $("input[name='j_password']").value(password)
        $("input[type='submit']").click()
    }
}
