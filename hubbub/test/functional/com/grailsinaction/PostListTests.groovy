package com.grailsinaction

/**
 * These tests are not the same as in the book - the ones in the book
 * are incorrect.
 *
 * Note that these tests will only work if you run them in the development
 * environment:
 *
 *   grails dev functional-tests
 */
class PostListTests extends functionaltestplugin.FunctionalTestCase {
    void testTimelineNotLoggedIn() {
        get("/post/global")
        assertTitle "Hubbub » Global Timeline"
    }

    void testTimeline() {
        // User must log in first.
        post("/j_spring_security_check") {
            j_username = "peter"
            j_password = "password"
        }

        // Check that the timeline page was loaded.
        assertTitle "Hubbub » MyTimeline for Peter Ledbrook"
        assertContentContains "What is Peter Ledbrook hacking on right now?"

        // Post a new message.
        def testMsg = "Functional test message"
        form("postMessage") {
            content = testMsg
            click "Post"
        }

        // Reload the page and check that the new post is displayed.
        assertContentContains testMsg

        // Make sure we're logged back out so we don't affect subsequent
        // tests.
        get("/logout")
    }
}
