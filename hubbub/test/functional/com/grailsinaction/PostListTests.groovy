package com.grailsinaction

class PostListTests extends functionaltestplugin.FunctionalTestCase {
    void testTimelineNotLoggedIn() {
        get("/post/list")
        assertStatus 500
    }

    void testTimeline() {
        // User must log in first.
        post("/login/index") {
            userId = "peter"
            password = "password"
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
