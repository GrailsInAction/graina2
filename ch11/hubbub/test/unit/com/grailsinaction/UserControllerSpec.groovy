package com.grailsinaction

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.*

@TestFor(UserController)
@Mock([User, Profile])
class UserControllerSpec extends Specification {

    def setup() {
        defineBeans {
            springSecurityService(grails.plugins.springsecurity.SpringSecurityService)
        }
    }

    def "Registering a user with known good parameters"() {

        given: "a set of user parameters"
        params.with {
            loginId = "glen_a_smith"
            password = "winnning"
        }

        and: 'a set of profile parameters'
        params['profile.fullName'] = "Glen Smith"
        params['profile.email'] = "glen@bytecode.com.au"
        params['profile.homepage'] = "http://blogs.bytecode.com.au/glen"

        when: "the user is registered"
        controller.register()

        then: "the user is created, and browser redirected"
        response.redirectedUrl == '/'
        User.count() == 1
        Profile.count() == 1
    }

    @Unroll
    def "Registration command objects for #loginId validating correctly"() {

        given: "a mocked command object"
        def urc = mockCommandObject(UserRegistrationCommand)

        and: "a set of initial values from the spock test"
        urc.loginId = loginId
        urc.password = password
        urc.passwordRepeat = passwordRepeat
        urc.fullName = "Your Name Here"
        urc.email = "someone@nowhere.net"

        when: "the validator is invoked"
        def isValidRegistration = urc.validate()

        then: "the appropriate fields are flagged as errors"
        isValidRegistration == anticipatedValid
        urc.errors.getFieldError(fieldInError)?.code == errorCode

        where:
        loginId | password   | passwordRepeat| anticipatedValid   | fieldInError       | errorCode
        "glen"  | "password" | "no-match"   | false               | "passwordRepeat"   | "validator.invalid"
        "peter" | "password" | "password"   | true                | null               | null
        "a"     | "password" | "password"   | false               | "loginId"          | "size.toosmall"

    }
    
}
