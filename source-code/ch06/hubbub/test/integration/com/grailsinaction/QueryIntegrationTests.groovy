package com.grailsinaction

import grails.test.*

class QueryIntegrationTests extends GrailsUnitTestCase {

    void testBasicDynamicFinders() {

		new User(userId: 'glen', password: 'secret',
            profile: new Profile(email: 'glen@glensmith.com')).save()
        new User(userId: 'peter', password: 'sesame',
            profile: new Profile(homepage: 'http://www.peter.com/')).save()

        def user = User.findByPassword('sesame')
        assertEquals 'peter', user.userId

        user = User.findByUserIdAndPassword('glen', 'secret')
        assertEquals 'glen', user.userId

        def now = new Date()
        def users = User.findAllByDateCreatedBetween(now-1, now)
        assertEquals 2, users.size()

        def profiles = Profile.findAllByEmailIsNotNull()
        assertEquals 1, profiles.size()

    }

    void testQueryByExample() {

        new User(userId: 'glen', password: 'password').save()
        new User(userId: 'peter', password: 'password').save()
        new User(userId: 'cynthia', password: 'sesame').save()

        def userToFind = new User(userId: 'glen')
        def u1 = User.find(userToFind)
        assertEquals('password', u1.password)

        userToFind = new User(userId: 'cynthia')
        def u2 = User.find(userToFind)
        assertEquals('cynthia', u2.userId)

        userToFind = new User(password: 'password')
        def u3 = User.findAll(userToFind)
        assert [ 'glen', 'peter'] == u3*.userId


    }


}
