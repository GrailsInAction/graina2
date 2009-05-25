package com.grailsinaction

import grails.converters.JSON

class PostRestController {
    def authenticateService
    def postService

    def list = {
        def postList = Post.list()
        withFormat {
            xml {
                [ posts: postList ]
            }
            json {
                render postList as JSON
            }
        }
    }

    def show = {
        def post = Post.get(params.id)
        if (post) {
            withFormat {
                xml {
                    [ post: post ]
                }
                json {
                    render post as JSON
                }
            }
        }
        else {
            response.sendError(404)
        }
    }

    def save = {
        def user = authenticateService.userDomain()
        def newContent = request.XML.content.text()
        def post = postService.createPost(user.id, newContent)

        if (post && !post.hasErrors()) {
            response.status = 201
            
            // The sample code in the book returns an empty string here,
            // but the ID of the new post is more useful.
            render contentType: "application/xml", {
                // Return the ID of the new post.
                id(post.id)
            }
        }
        else {
            response.status = 403
            render contentType: "text/xml", encoding: "utf-8", {
                errors {
                    post?.errors?.fieldErrors?.each { err ->
                        field(err.field)
                        message(g.message(error: err))
                    }
                }
            }
        }
    }
    
    def update = {
        def post = Post.get(params.id)
        if (!post) { response.sendError(404); return }

        def newContent = request.XML.content.text()
        post.content = newContent

        if (post.validate()) {
            render ""
        }
        else {
            response.status = 403
            render contentType: "application/xml", {
                errors {
                    post.errors.fieldErrors.each { err ->
                        validation {
                            field(err.field)
                            message(g.message(error: err))
                        }
                    }
                }
            }
        }
    }
}
