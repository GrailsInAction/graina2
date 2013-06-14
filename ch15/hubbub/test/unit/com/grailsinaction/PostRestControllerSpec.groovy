package com.grailsinaction

import grails.plugins.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import grails.test.mixin.Mock
import spock.lang.Specification

@TestFor(PostRestController)
@Mock([User,Post])
class PostRestControllerSpec extends Specification {

    void setup() {
        defineBeans {
            springSecurityService(SpringSecurityService)
        }
    }

    void "GET a list of posts as JSON"() {
        given: "A set of posts"
        initialiseUsersAndPosts()

        when: "I invoke the show action without an ID"
        controller.show()

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

        when: "I invoke the show action without an ID and requesting XML"
        response.format = "xml"
        controller.show()

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

        when: "I invoke the show action without an ID and requesting XML"
        response.format = "xml"
        controller.show(1)

        then: "I get the expected posts as an XML document"
        response.xml.content.text() == "A first post"
    }

    void "POST a single post as JSON"() {
        given: "A set of existing posts"
        def userId = initialiseUsersAndPosts()

        when: "I invoke the save action with a JSON packet"
        request.json = '{"post": {"content": "A new post!", "user.id": ' + userId + '}}'
        controller.save()

        then: "I get a 201 JSON response with the ID of the new post"
        response.status == 201
        response.json.id instanceof Number
    }

    void "POST a single post as XML"() {
        given: "A set of existing posts"
        def userId = initialiseUsersAndPosts()

        when: "I invoke the save action with a JSON packet"
        request.xml = '<post><content>A new post!</content><user id="' + userId + '"/></post>'
        response.format = "xml"
        controller.save()

        then: "I get a 201 XML response with the ID of the new post"
        response.status == 201
        response.xml.entry.@key.text() == "id"
        response.xml.entry.text().isNumber()

        def id = response.xml.entry.text().toInteger()
        Post.get(id).user.id == userId
    }

    private initialiseUsersAndPosts() {
        def securityService = Mock(SpringSecurityService) {
            _ * encodePassword(_) >> "aksjhdsa"
        }

        def chuck = new User(loginId: "chuck_norris", password: "password")
        chuck.springSecurityService = securityService
        chuck.addToPosts(content: "A first post")
        chuck.addToPosts(content: "A second post")
        chuck.save(failOnError: true)

        def bruce = new User(loginId: "bruce_lee", password: "kungfu")
        bruce.springSecurityService = securityService
        bruce.addToPosts(content: "Soaking up the sun")
        bruce.addToPosts(content: "Preparing for battle")
        bruce.save(failOnError: true, flush: true)

        return chuck.id
    }
}

