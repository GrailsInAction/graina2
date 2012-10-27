package com.grailsinaction

import grails.converters.*

class PostController {

    def postService

    def scaffold = true

    def index = {
        if (!params.id)
            params.id = "chuck_norris"
        redirect(action: 'timeline', params: params)
    }

    def timeline(String id) {
        def user = User.findByUserId(id)
        if (!user) {
            response.sendError(404) // user not found
        } else {
            [ user : user ]
        }
    }

    /*
    New version of post using a service layer
    */
    def addPost(String id, String content) {
        try {
            def newPost = postService.createPost(id, content)
            flash.message = "Added new post: ${newPost.content}"
        } catch (PostException pe) {
            flash.message = pe.message
        }
        redirect(action: 'timeline', id: id)
    }



    def recentPosts() {

        def user = User.findByUserId(params.id)
        def posts = Post.findAllByUser(user, [max: 5])

        withFormat {
            js { 
                render(contentType:"text/json") {
                    hubbubPosts(user: user.userId) {
                        posts.each { p ->
                            post(contents: p.content,
                                created:p.dateCreated)
                        }
                    }
				
                }
            }
            xml { render posts.encodeAsXML() }
        }


    }

   

    /* Refactor old addPost() action into the PostService */
    /*
    def addPost() {
        def user = User.findByUserId(params.id)
        if (user) {
            def post = new Post(params)
            user.addToPosts(post)
            if (post.validate() && post.save(failOnError: true)) {
                flash.message = "Successfully created Post"
            } else {
                flash.message = "Invalid or empty post"
            }
        } else {
            flash.message = "Invalid User Id"
        }
        redirect(action: 'timeline', id: params.id)
    }
    */

}
