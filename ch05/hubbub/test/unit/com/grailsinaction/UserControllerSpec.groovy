package com.grailsinaction

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(UserController)
@Mock([User, Profile, LameSecurityFilters])
class UserControllerSpec extends Specification {


	def "Registering a user with known good parameters"() {

		given: "a set of user parameters"
		params.with {
			userId = "glen_a_smith"
			password = "winnning"
            homepage = "http://blogs.bytecode.com.au/glen"
		}

        and: 'a set of profile parameters'
        params['profile.fullName'] = "Glen Smith"
        params['profile.email'] = "glen@bytecode.com.au"

		when: "the user is registered"
		controller.register()

		then: "the user is created, and browser redirected"
		response.redirectedUrl == '/'
        User.count() == 1
        Profile.count() == 1

	}

    @Unroll
    def "Registration command objects for #userId validating correctly"() {

        given: "a mocked command object"
        def urc = mockCommandObject(UserRegistrationCommand)

        and: "a set of initial values from the spock test"
        urc.userId = userId
        urc.password = password
        urc.passwordRepeat = passwordRepeat
        urc.fullName = "Your Name Here"

        when: "the validator is invoked"
        def isValidRegistration = urc.validate()

        then: "the appropriate fields are flagged as errors"
        isValidRegistration == anticipatedValid
        urc.errors.getFieldError(fieldInError)?.code == errorCode
        println urc.errors

        where:
        userId  | password   | passwordRepeat| anticipatedValid   | fieldInError       | errorCode
        "glen"  | "password" | "no-match"   | false               | "passwordRepeat"   | "validator.invalid"
        "peter" | "password" | "password"   | true                | null               | null
        "a"     | "password" | "password"   | false               | "userId"           | "size.toosmall"

    }

    def "Invoking the new register action via a command object"() {

        given:  "A configured command object"
        def urc = mockCommandObject(UserRegistrationCommand)
        urc.with {
            userId = "glen_a_smith"
            fullName = "Glen Smith"
            password = "password"
            passwordRepeat = "password"
        }

        and:  "which has been validated"
        urc.validate()

        when:  "the register is invoked"
        controller.register2(urc)

        then: "the user is registered and browser redirected"
        !urc.hasErrors()
        response.redirectedUrl == '/'
        User.count() == 1
        Profile.count() == 1

    }

}
