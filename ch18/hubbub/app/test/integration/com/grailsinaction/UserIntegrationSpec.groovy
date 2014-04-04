package com.grailsinaction

import spock.lang.*

class UserIntegrationSpec extends Specification {
    
    def dumbster

    def "Saving our first user to the database"() {

        given: "A brand new user"
        def joe = new HubbubUser(loginId: 'joe', passwordHash: 'secret')

        when: "the user is saved"
        joe.save()

        then: "it saved successfully and can be found in the database"
        joe.errors.errorCount == 0
        joe.id != null
        User.get(joe.id).loginId == joe.loginId

    }

    def "Updating a saved user changes its properties"() {

        given: "An existing user"
        def existingUser = new HubbubUser(loginId: 'joe', passwordHash: 'secret')
        existingUser.save(failOnError: true)

        when: "A property is changed"
        def foundUser = User.get(existingUser.id)    
        foundUser.loginId = 'jane'
        foundUser.save(failOnError: true)

        then: "The change is reflected in the database"
        User.get(existingUser.id).loginId == 'jane'

    }
    
    def "Deleting an existing user removes it from the database"() {

        given: "An existing user"
        def user = new HubbubUser(loginId: 'joe', passwordHash: 'secret')
        user.save(failOnError: true)

        when: "The user is deleted"
        def foundUser = User.get(user.id)
        foundUser.delete(flush: true)

        then: "The user is removed from the database"
        !User.exists(foundUser.id)

    }
    
    def "Saving a user with invalid properties causes an error"() {

        given: "A user which fails several field validations"
        def user = new HubbubUser(loginId: 'me', passwordHash: 'tiny')

        when:  "The user is validated"
        user.validate()

        then:
        user.hasErrors()

        user.errors.getFieldError("loginId")

        // 'homepage' is now on the Profile class, so is not validated.
    
        // The password is no longer validated on the domain class because
        // its SHA256 hash is stored instead.
    }

    def "Recovering from a failed save by fixing invalid properties"() {

        given: "A user that has invalid properties"
        def chuck = new HubbubUser(loginId: 'me', passwordHash: 'tiny')
        assert chuck.save()  == null
        assert chuck.hasErrors()

        when: "We fix the invalid properties"
        chuck.loginId = "joe"
        chuck.validate()

        then: "The user saves and validates fine"
        !chuck.hasErrors()
        chuck.save()
    
    }

    def "Ensure a user can follow other users"() {

        given: "A set of baseline users"
        def joe = new HubbubUser(loginId: 'joe', passwordHash:'password').save()
        def jane = new HubbubUser(loginId: 'jane', passwordHash:'password').save()
        def jill = new HubbubUser(loginId: 'jill', passwordHash:'password').save()

        when: "Joe follows Jane & Jill, and Jill follows Jane"
        joe.addToFollowing(jane)
        joe.addToFollowing(jill)
        jill.addToFollowing(jane)

        then: "Follower counts should match following people"
        2 == joe.following.size()
        1 == jill.following.size()
        
    }

    def "Welcome email is generated and sent"() {

        given: "An empty inbox"
        dumbster.reset()

        and: "a user controller"
        def userController = new UserController()

        when: "A welcome email is sent"
        userController.welcomeEmail("tester@email.com")


        then: "It appears in their inbox"
        dumbster.messageCount == 1
        def msg = dumbster.getMessages().first()
        msg.subject ==  "Welcome to Hubbub!"
        msg.to == "tester@email.com"
        msg.body =~ /The Hubbub Team/

    }
    
}
