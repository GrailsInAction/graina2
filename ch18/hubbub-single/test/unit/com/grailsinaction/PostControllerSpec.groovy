package com.grailsinaction

import grails.plugins.springsecurity.SpringSecurityService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification


@TestFor(PostController)
@Mock([User,Post,LameSecurityFilters])
class PostControllerSpec extends Specification {
    def mockSecurityService

    def setup() {
        mockSecurityService = Mock(SpringSecurityService)
        mockSecurityService.encodePassword(_ as String) >> "kjsdfhkshfalhlkdshflas"
    }

    def "Get a users timeline given their id"() {
        given: "A user with posts in the db"
        User chuck = new User(loginId: "chuck_norris")
        chuck.springSecurityService = mockSecurityService
        chuck.password = "password"
        chuck.addToPosts(new Post(content: "A first post"))
        chuck.addToPosts(new Post(content: "A second post"))
        chuck.save(failOnError: true)

        and: "A loginId parameter"
        params.id = chuck.loginId

        when: "the timeline is invoked"
        def model = controller.timeline()

        then: "the user is in the returned model"
        model.user.loginId == "chuck_norris"
        model.user.posts.size() == 2
    }

    def "Check that non-existent users are handled with an error"() {

        given: "the id of a non-existent user"
        params.id = "this-user-id-does-not-exist"

        when: "the timeline is invoked"
        controller.timeline()

        then: "a 404 is sent to the browser"
        response.status == 404

    }

    def "Adding a valid new post to the timeline"() {
        given: "mock post and security services"
        def mockPostService = Mock(PostService)
        1 * mockPostService.createPost(_, _) >> new Post(content: "Mock Post")
        controller.postService = mockPostService

        def securityService = Mock(SpringSecurityService)
        _ * securityService.getCurrentUser() >> new User(loginId: "chuck_norris")
        controller.springSecurityService = securityService

        when: "addPost is invoked with a login ID and some post content"
        def model = controller.addPost("Mock Post")

        then: "our flash message and redirect confirms the success"
        flash.message ==~ /Added new post: Mock.*/
        response.redirectedUrl == "/post/timeline/chuck_norris"
    }

    def "Adding an invalid new post to the timeline trips an error"() {
        given: "mock post and security services"
        def mockPostService = Mock(PostService)
        1 * mockPostService.createPost(_, _) >> { throw new PostException(message: "Invalid or empty post") }
        controller.postService = mockPostService

        def securityService = Mock(SpringSecurityService)
        _ * securityService.getCurrentUser() >> new User(loginId: "chuck_norris")
        controller.springSecurityService = securityService

        when: "addPost is invoked with a login ID but no post content"
        def model = controller.addPost(null)

        then: "our flash message and redirect confirms the failure"
        flash.message == "Invalid or empty post"
        response.redirectedUrl == "/post/timeline/chuck_norris"
    }

    @spock.lang.Unroll
    def "Testing id of #suppliedId redirects to #expectedUrl"() {

        given:
        params.id = suppliedId

        when: "Controller is invoked"
        controller.index()

        then:
        response.redirectedUrl == expectedUrl

        where:
        suppliedId  |   expectedUrl
        'joe_cool'  |   '/post/timeline/joe_cool'
        null        |   '/post/timeline/chuck_norris'

    }

    def "Exercising security filter invocation for unauthenticated user"() {

        when:
        withFilters(action: "addPost") {
            controller.addPost("glen_a_smith", "A first post")
        }

        then:
        response.redirectedUrl == '/login/form'

    }

}
