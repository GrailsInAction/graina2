package com.grailsinaction

class AccessControlTests extends functionaltestplugin.FunctionalTestCase {
    void testAnonymousAccess() {
        // First try the home page.
        get("/")
        checkHomePage()
    }

    void testTimeline() {
        // Try to access the personal timeline page. Doing so should
        // redirect us to the login page.
        get("/post/personal")
        checkHomePage()

        // Log in with the incorrect password - should end up back at
        // the login (home) page.
        login("peter", "test")
        checkHomePage()

        // Log in with the correct password this time and make sure
        // that we are redirected to the personal timeline page. The
        // double login is intentional - otherwise the tests fail.
        login()
        login()
        checkTimelinePage()

        get("/logout")
        get("/post/personal")
        checkHomePage()
    }

    void checkHomePage() {
        assertTitle "Hubbub » Global Timeline"
    }

    void checkTimelinePage() {
        assertTitle "Hubbub » MyTimeline for Peter Ledbrook"
        assertContentContains "What is Peter Ledbrook hacking on right now?"
    }

    void login(userId = "peter", password = "password") {
        // Do the login.
        post("/j_spring_security_check") {
            j_username = userId
            j_password = password
        }
    }
}
