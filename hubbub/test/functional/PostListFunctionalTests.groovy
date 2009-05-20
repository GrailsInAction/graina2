class PostListFunctionalTests extends functionaltestplugin.FunctionalTestCase {
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
