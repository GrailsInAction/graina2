package com.grailsinaction

/**
 * Test case for {@link User}.
 */
class UserUnitTests extends grails.test.GrailsUnitTestCase {
    void testConstraints() {
        def will = new User(userId: "william")
        mockForConstraintsTests(User, [ will ])

        def testUser = new User()
        assertFalse testUser.validate()
        assertEquals "nullable", testUser.errors["userId"]
        assertEquals "nullable", testUser.errors["password"]

        testUser = new User(userId: "william", password: "william")
        assertFalse testUser.validate()
        assertEquals "unique", testUser.errors["userId"]
        assertEquals "validator", testUser.errors["password"]

        testUser = new User(userId: "me", password: "tempo")
        assertFalse testUser.validate()
        assertEquals "size", testUser.errors["userId"]
        assertEquals "size", testUser.errors["password"]

        testUser = new User(userId: "m" * 21, password: "p" * 61)
        assertFalse testUser.validate()
        assertEquals "size", testUser.errors["userId"]
        assertEquals "size", testUser.errors["password"]

        testUser = new User(userId: "glen", password: "passwd")
        assertTrue testUser.validate()
    }
}
