package com.grailsinaction

import grails.test.mixin.TestFor
import spock.lang.Specification
import grails.test.mixin.Mock

@TestFor(PostController)
@Mock(User)
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

      then:
      response.status == 404

    }

}
