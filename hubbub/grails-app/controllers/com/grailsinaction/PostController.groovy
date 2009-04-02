package com.grailsinaction

import grails.converters.*

class PostController {

    def postService

    def scaffold = true

    static navigation = [
        [group: 'tabs', action: 'personal', title: 'My Posts', order: 0],
        [action:'timeline', title: 'My Timeline', order: 1],
        [action: 'global', title: 'Everyone', order: 2],

    ]

    def index = {
        if (!params.id)
            params.id = "chuck_norris"
        redirect(action: 'timeline', params: params)
    }

    def timeline = {
        def user = User.findByUserId(params.id)
        [ user : user ]
    }

    def addPost = {
        try {
            def newPost = postService.createPost(params.id, params.content)
            flash.message = "Added new post: ${newPost.content}"
        } catch (PostException pe) {
            flash.message = pe.message
        }
        redirect(action: 'timeline', id: params.id)
    }


    def recentPosts = {

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
    def addPost = {
        def user = User.findByUserId(params.id)
        if (user) {
            def post = new Post(params)
            if (post.validate()) {
                user.addToPosts(post)
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
