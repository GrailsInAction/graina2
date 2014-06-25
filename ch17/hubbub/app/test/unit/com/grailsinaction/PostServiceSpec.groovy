package com.grailsinaction

import spock.lang.*
import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import grails.test.mixin.Mock

@TestFor(PostService)
@Mock([HubbubUser,Post])
class PostServiceSpec extends Specification {

    def "Valid posts get saved and added to the user"() {

        given: "A new user in the db"
        User chuck = new HubbubUser(loginId: "chuck_norris")
        chuck.passwordHash = "ksadhfkasjdfh"
        chuck.save(failOnError: true)

        and: "a mock event() method"
        def eventTriggered = false
        PostService.metaClass.event = { String event, msg ->
            eventTriggered = true
            assert event == "onNewPost"
        }

        when: "a new post is created by the service"
        def newPost = service.createPost("chuck_norris", "First Post!")

        then: "the post returned and added to the user"
        newPost.content == "First Post!"
        User.findByLoginId("chuck_norris").posts.size() == 1

        and: "the new post event was fired"
        eventTriggered

    }

    def "Invalid posts generate exceptional outcomes"() {

        given: "A new user in the db"
        def chuck = new HubbubUser(loginId: "chuck_norris")
        chuck.passwordHash = "ksadhfkasjdfh"
        chuck.save(failOnError: true)

        when: "an invalid post is attempted"
        def newPost = service.createPost("chuck_norris", null)

        then: "an exception is thrown and no post is saved"
        thrown(PostException)

    }
    
}
