package com.grailsinaction

import grails.test.mixin.TestFor
import spock.lang.Specification
import grails.test.mixin.Mock
import spock.lang.IgnoreRest


@TestFor(PostController)
@Mock([User,Post,LameSecurityFilters])
class PostControllerSpec extends Specification {

    def "Get a users timeline given their id"() {
      given: "A user with posts in the db"
      User chuck = new User(userId: "chuck_norris", password: "password").save(failOnError: true)
      chuck.addToPosts(new Post(content: "A first post"))
      chuck.addToPosts(new Post(content: "A second post"))

      and: "A userid parameter"
      params.id = chuck.userId

      when: "the timeline is invoked"
      def model = controller.timeline()

      then: "the user is in the returned model"
      model.user.userId == "chuck_norris"
      model.user.posts.size() == 2
    }

    def "Check that non-existent users are handled with an error"() {

      given: "the is of a non-existeant user"
      params.id = "this-user-id-does-not-exist"

      when: "the timeline is invoked"
      controller.timeline()

      then: "a 404 is sent to the browser"
      response.status == 404

    }

    def "Adding a valid new post to the timeline"() {
      given: "A user with posts in the db"
      User chuck = new User(userId: "chuck_norris", password: "password").save(failOnError: true)

      and: "A userid parameter"
      params.id = chuck.userId

      and: "Some content for the post"
      params.content = "Chuck Norris can unit test entire applications with a single assert."

      when: "addPost is invoked"
      def model = controller.addPost()

      then: "our flash message and redirect confirms the success"
      flash.message == "Successfully created Post"
      response.redirectedUrl == "/post/timeline/${chuck.userId}"
      Post.countByUser(chuck) == 1

    }

    def "Adding a invalid new post to the timeline trips an error"() {
        given: "A user with posts in the db"
        User chuck = new User(userId: "chuck_norris", password: "password").save(failOnError: true)

        and: "A userid parameter"
        params.id = chuck.userId

        and: "Some content for the post"
        params.content = null

        when: "addPost is invoked"
        def model = controller.addPost()

        then: "our flash message and redirect confirms the success"
        flash.message == "Invalid or empty post"
        response.redirectedUrl == "/post/timeline/${chuck.userId}"
        Post.countByUser(chuck) == 0

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

    def "Adding new post via mocked service layer honours functionality"() {

        given: "a mock post service"
        def mockPostService = Mock(PostService)
        1 * mockPostService.createPost(_, _) >> new Post(content: "Mock Post")
        controller.postService = mockPostService


        when:  "controller is invoked"
        def result = controller.addPost("joe_cool", "Posting up a storm")

        then: "redirected to timeline, flash message tells us all is well"
        flash.message ==~ /Added new post: Mock.*/
        response.redirectedUrl == '/post/timeline/joe_cool'

    }

    @IgnoreRest
    def "Exercising security filter invocation for unauthenticated user"() {

        when:
        withFilters(action:"post") {
            controller.addPost("glen_a_smith", "A first post")
        }

        then:
        response.redirectedUrl == '/login/form'

    }

}
