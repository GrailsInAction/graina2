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
        assertContentContains "What is Chuck Norris hacking on right now?"
    }

    void testTimeline() {
        get("/post/global?login=peter")
        assertTitle "Hubbub » Global Timeline"
        assertContentContains "What is Peter Ledbrook hacking on right now?"
    }
}
