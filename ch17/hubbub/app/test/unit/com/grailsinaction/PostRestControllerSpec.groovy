package com.grailsinaction

import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

@TestFor(PostRestController)
@Mock([HubbubUser,User,UserRole,Post])
class PostRestControllerSpec extends Specification {

    void setup() {
        JSON.createNamedConfig("v1") { cfg -> }
    }

    void "GET a list of posts as JSON"() {
        given: "A set of posts"
        initialiseUsersAndPosts()

        when: "I invoke the index action"
        controller.index()

        then: "I get the expected posts as a JSON list"
        response.json*.content.sort() == [
                "A first post",
                "A second post",
                "Preparing for battle",
                "Soaking up the sun" ]
    }

    void "GET a list of posts as XML"() {
        given: "A set of posts"
        initialiseUsersAndPosts()

        when: "I invoke the index action requesting XML"
        response.format = "xml"
        controller.index()

        then: "I get the expected posts as an XML document"
        response.xml.post.content*.text().sort() == [
                "A first post",
                "A second post",
                "Preparing for battle",
                "Soaking up the sun" ]
    }

    void "GET a single post as XML"() {
        given: "A set of posts"
        initialiseUsersAndPosts()

        when: "I invoke the show action requesting XML"
        response.format = "xml"
        controller.show(1, "1")

        then: "I get the expected posts as an XML document"
        response.xml.content.text() == "A first post"
    }

    void "POST a single post as JSON"() {
        given: "A set of existing posts"
        def userId = initialiseUsersAndPosts()
        def message = "A new post!"

        and: "mock security and post services"
        def securityService = Mock(SpringSecurityService) {
            getCurrentUser() >> [loginId: userId]
        }
        controller.springSecurityService = securityService

        controller.postService = Mock(PostService) {
            1 * createPost(userId, message) >> new Post(content: message)
        }

        when: "I invoke the save action with a JSON packet"
        request.json = '{"message": "' + message + '"}'
        controller.save()

        then: "I get a 201 JSON response with the ID of the new post"
        response.status == 201
        response.json.class == "com.grailsinaction.Post"
        response.json.content == message
    }

    void "POST a single post as XML"() {
        given: "A set of existing posts"
        def userId = initialiseUsersAndPosts()
        def message = "A new post!"

        and: "mock security and post services"
        def securityService = Mock(SpringSecurityService) {
            getCurrentUser() >> [loginId: userId]
        }
        controller.springSecurityService = securityService

        controller.postService = Mock(PostService) {
            1 * createPost(userId, message) >> new Post(id: 100, content: message)
        }

        when: "I invoke the save action with an XML packet"
        request.xml = '<post><message>' + message + '</message></post>'
        response.format = "xml"
        controller.save()

        then: "I get a 201 XML response with the ID of the new post"
        response.status == 201
        response.xml.content.text() == message
    }

    private initialiseUsersAndPosts() {
        def chuck = new HubbubUser(loginId: "chuck_norris", passwordHash: "password")
        chuck.addToPosts(content: "A first post")
        chuck.addToPosts(content: "A second post")
        chuck.save(failOnError: true)

        def bruce = new HubbubUser(loginId: "bruce_lee", passwordHash: "kungfu")
        bruce.addToPosts(content: "Soaking up the sun")
        bruce.addToPosts(content: "Preparing for battle")
        bruce.save(failOnError: true, flush: true)

        return chuck.loginId
    }
}

