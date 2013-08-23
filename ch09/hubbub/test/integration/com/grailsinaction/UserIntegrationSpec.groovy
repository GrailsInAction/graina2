package com.grailsinaction

import spock.lang.*
import grails.plugin.spock.*

class UserIntegrationSpec extends IntegrationSpec  {

    def "Saving our first user to the database"() {

        given: "A brand new user"
        def joe = new User(loginId: 'joe', password: 'secret')

        when: "the user is saved"
        joe.save()

        then: "it saved successfully and can be found in the database"
        joe.errors.errorCount == 0
        joe.id != null
        User.get(joe.id).loginId == joe.loginId

    }


    def "Updating a saved user changes its properties"() {

        given: "An existing user"
        def existingUser = new User(loginId: 'joe', password: 'secret').save(failOnError: true)

        when: "A property is changed"
        def foundUser = User.get(existingUser.id)    
        foundUser.password = 'sesame'                   
        foundUser.save(failOnError: true)              

        then: "The change is reflected in the database"
        User.get(existingUser.id).password == 'sesame'

    }

    def "Deleting an existing user removes it from the database"() {

        given: "An existing user"
        def user = new User(loginId: 'joe', password: 'secret').save(failOnError: true)

        when: "The user is deleted"
        def foundUser = User.get(user.id)
        foundUser.delete(flush: true)

        then: "The user is removed from the database"
        !User.exists(foundUser.id)            
    }

    def "Saving a user with invalid properties causes an error"() {

        given: "A user which fails several field validations"
        def user = new User(loginId: 'chuck_norris', password: 'tiny')

        when:  "The user is validated"
        user.validate()

        then:
        user.hasErrors()

        "size.toosmall" == user.errors.getFieldError("password").code                     
        "tiny" == user.errors.getFieldError("password").rejectedValue
        !user.errors.getFieldError("loginId")

        // 'homepage' is now on the Profile class, so is not validated.
    
    }

    def "Recovering from a failed save by fixing invalid properties"() {

        given: "A user that has invalid properties"
        def chuck = new User(loginId: 'chuck_norris', password: 'tiny')
        assert chuck.save()  == null
        assert chuck.hasErrors()

        when: "We fix the invalid properties"
        chuck.password = "fistfist"

        // 'homepage' is now on Profile so can't be set on the user.

        then: "The user saves and validates fine"
        chuck.validate()
        !chuck.hasErrors()
        chuck.save()
    
    }

    def "Ensure a user can follow other users"() {

        given: "A set of baseline users"
        def glen = new User(loginId: 'glen', password:'password').save()
        def peter = new User(loginId: 'peter', password:'password').save()
        def sven = new User(loginId: 'sven', password:'password').save()

        when: "Glen follows Peter, and Sven follows Peter"
        glen.addToFollowing(peter)                     
        glen.addToFollowing(sven)          
        sven.addToFollowing(peter)

        then: "Follower counts should match following people"
        2 == glen.following.size()
        1 == sven.following.size()
        
    }




}
