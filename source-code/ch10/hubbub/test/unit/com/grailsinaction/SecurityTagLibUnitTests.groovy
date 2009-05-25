package com.grailsinaction

class SecurityTagLibUnitTests extends grails.test.TagLibUnitTestCase {
    void testIsLoggedIn() {
        def testContent = "The content to show"
        mockSession["user"] = "me"
        tagLib.isLoggedIn([:]) {-> testContent }

        assertEquals testContent, tagLib.out.toString()
    }

    void testIsLoggedInNoUser() {
        def testContent = "The content not to show"
        tagLib.isLoggedIn([:]) {-> testContent }

        assertEquals "", tagLib.out.toString()
    }
}
