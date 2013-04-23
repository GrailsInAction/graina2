package com.grailsinaction

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(PostService)
@Mock([User,Post])
class PostServiceSpec extends Specification {

    def "Valid posts get saved and added to the user"() {

        given: "A new user in the db"
        new User(loginId: "chuck_norris", password: "password").save(failOnError: true)

        when: "a new post is created by the service"
        def newPost = service.createPost("chuck_norris", "First Post!")

        then: "the post returned and added to the user"
        newPost.content == "First Post!"
        User.findByLoginId("chuck_norris").posts.size() == 1

    }

    def "Invalid posts generate exceptional outcomes"() {

        given: "A new user in the db"
        new User(loginId: "chuck_norris", password: "password").save(failOnError: true)

        when: "an invalid post is attempted"
        def newPost = service.createPost("chuck_norris", null)

        then: "an exception is thrown and no post is saved"
        thrown(PostException)

    }

}
