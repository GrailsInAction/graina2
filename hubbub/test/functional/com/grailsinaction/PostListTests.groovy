package com.grailsinaction

class PostListTests extends functionaltestplugin.FunctionalTestCase {
    void testTimelineNotLoggedIn() {
        get("/post/list")
        assertTitle "Hubbub » Howdy"
    }

    void testTimeline() {
        // User must log in first.
        post("/j_spring_security_check") {
            j_username = "peter"
            j_password = "password"
        }

        // Check that the timeline page was loaded.
        assertTitle "Hubbub » New Post"
        assertContentContains "What are you hacking on right now?"

        // Post a new message.
        def testMsg = "Functional test message"
        form("postMessage") {
            postContent = testMsg
            click "Post"
        }

        assertContentContains testMsg
    }
}
