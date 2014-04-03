package com.grailsinaction

import grails.converters.*

class PostRestController {
    static responseFormats = ["json", "xml"]

    def postService
    def springSecurityService

    def index(String v) {
        def configName = 'v' + (v ?: 1)
        
        JSON.use(configName) {
            respond Post.list()
        }
    }

    def show(Integer id, String v) {
        def configName = 'v' + (v ?: 1)
        
        JSON.use(configName) {
            respond Post.get(id)
        }
    }



    def save(PostDetails post) {
        if (!post.hasErrors()) {
            def user = springSecurityService.currentUser
            def newPost = postService.createPost(
                    user.loginId,
                    post.message)
            respond newPost, status: 201
        }
        else {
            respond post
        }
    }

    def update(Long id) {
        if (id == null) {
            response.sendError 405
            return
        }

        def post = Post.get(id)
        if (post) {
            //bindData post, params.post
            post.content = params.message

            def body
            if (post.validate() && post.save()) {
                response.status = 200
                body = [id: post.id]
                //render status: 200, [id: post.id] as XML
            }
            else {
                response.status = 400
                body = [error: "Invalid data"]
                //render status: 400, [error: "Invalid data"] as XML
            }
            withFormat {
                json { render body as JSON }
                xml { render body as XML }
            }
        }
        else {
            response.sendError 404
        }
    }

    def delete(Long id) {
        def body

        if (id == null) {
            response.status = 405
            body = [message: "Cannot DELETE without an ID"]
        }

        if (Post.exists(id)) {
            Post.load(id).delete()
            body = [message: "Post with ID $id deleted"]
        }
        else {
            response.status = 404
            body = [message: "Not found"]
        }

        withFormat {
            json {
                render body as JSON
            }

            xml {
                render body as XML
            }
        }
    }
    
}

class PostDetails {
    String message

    static constraints = {
        message blank: false, nullable: false
    }
}
