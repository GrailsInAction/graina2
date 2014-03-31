package com.grailsinaction

import com.grailsinaction.pages.*
import geb.spock.GebReportingSpec

class TimelinePageFunctionalSpec extends GebReportingSpec {
    def "Does timeline load for user 'phil'"() {
        when: "We navigate to the timeline page for the user 'phil'"
        to TimelinePage, "phil"

        then: "The correct heading is displayed with his full name"
        whatHeading.text() == "What is Phil Potts hacking on right now?"
    }

    def "Submitting a new post"() {
        given: "I log in and start at my timeline page"
        login "frankie", "testing"
        to TimelinePage, "phil"

        when: "I enter a new message and post it"
        newPostContent.value("This is a test post from Geb")
        submitPostButton.click()

        then: "I see the new post in the timeline"
        waitFor { !posts("This is a test post from Geb").empty }
    }

    private login(String username, String password) {
        to LoginPage
        loginIdField = username
        passwordField = password
        signInButton.click()
    }
}
