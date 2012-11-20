package com.grailsinaction

import grails.test.*

class UserIntegrationTests extends GrailsUnitTestCase {
    
    void testFirstSaveEver() {

		def user = new User(userId: 'joe', password: 'secret',
            homepage: 'http://www.grailsinaction.com')
		assertNotNull user.save()
		assertNotNull user.id

		def foundUser = User.get(user.id)
		assertEquals 'joe', foundUser.userId

	}

    void testSaveAndUpdate() {

		def user = new User(userId: 'joe', password: 'secret',
            homepage: 'http://www.grailsinaction.com')
		assertNotNull user.save()

		def foundUser = User.get(user.id)
		foundUser.password = 'sesame'
		foundUser.save()

		def editedUser = User.get(user.id)
		assertEquals 'sesame', editedUser.password

    }


    void testSaveThenDelete() {

        def user = new User(userId: 'joe', password: 'secret',
			homepage: 'http://www.grailsinaction.com')
        assertNotNull user.save()

        def foundUser = User.get(user.id)
        foundUser.delete()

        assertFalse User.exists(foundUser.id)

    }

    void testEvilSave() {
		def user = new User(userId: 'chuch_norris',
            password: 'tiny', homepage: 'not-a-url')
        
        assertFalse user.validate()
        assertTrue user.hasErrors()
        def errors = user.errors
        
        assertEquals "size.toosmall",
            errors.getFieldError("password").code
        assertEquals "tiny",
            errors.getFieldError("password").rejectedValue

        /*
        assertEquals "url.invalid",
            errors.getFieldError("homepage").code
        assertEquals "not-a-url",
            errors.getFieldError("homepage").rejectedValue
        */
        assertNull errors.getFieldError("userId")

    }

    void testEvilSaveCorrected() {
        def user = new User(userId: 'chuch_norris',
            password: 'tiny', homepage: 'not-a-url')
		assertFalse(user.validate())
        assertTrue(user.hasErrors())

		user.password = "fistfist"
		/*
        user.homepage = "http://www.chucknorrisfacts.com"
		*/
        assertTrue(user.validate())
        assertFalse(user.hasErrors())

    }

    void testFollowing() {
        def glen = new User(userId: 'glen', password:'password').save()
        def peter = new User(userId: 'peter', password:'password').save()
        def sven = new User(userId: 'sven', password:'password').save()

        glen.addToFollowing(peter)
        glen.addToFollowing(sven)
        assertEquals 2, glen.following.size()

        sven.addToFollowing(peter)
        assertEquals 1, sven.following.size()
    }



}
