package com.grailsinaction

class AccessControlTests extends functionaltestplugin.FunctionalTestCase {
    void testAnonymousAccess() {
        // First try the home page.
        get("/")
        checkHomePage()
    }

    void testProtectedPage() {
        // Try to access the personal timeline page. Doing so should
        // redirect us to the login page.
        get("/post/list")
        checkHomePage()

        // Log in with the incorrect password - should end up back at
        // the login (home) page.
        login("peter", "test")
        checkHomePage()

        // Log in with the correct password this time and make sure
        // that we are redirected to the personal timeline page.
        login()
        login()
        checkTimelinePage()

        post("/logout")
        get("/post/list")
        checkHomePage()
    }

    void checkHomePage() {
        assertTitle "Hubbub » Howdy"
    }

    void checkTimelinePage() {
        assertTitle "Hubbub » New Post"
        assertContentContains "What are you hacking on right now?"
    }

    void login(userId = "peter", password = "password") {
        // Do the login.
        post("/j_spring_security_check") {
            j_username = userId
            j_password = password
        }
    }
}
