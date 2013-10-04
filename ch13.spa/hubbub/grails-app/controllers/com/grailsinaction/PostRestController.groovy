package com.grailsinaction

import grails.converters.JSON
import grails.converters.XML
import org.springframework.validation.Errors
import org.springframework.validation.FieldError

class PostRestController {
    transient springSecurityService

    def list() {
        def body = Post.list(params)
        println ">> Requested version ${params.v}"

        withFormat {
            json { render body as JSON }
            xml { render text: (body as XML).toString(), contentType: "application/xml" }
        }
    }

    def show(Long id) {
        def body =  Post.get(id)

        withFormat {
            json { render body as JSON }
            xml { render body as XML }
        }
    }

    def save() {
        def body
        def post = new Post(params.post)
        post.user = springSecurityService.currentUser
        if (post.validate() && post.save()) {
            response.status = 201
            body = [id: post.id]
        }
        else {
            response.status = 403
            body = [errors: createErrorList(post.errors)]
        }

        withFormat {
            json { render body as JSON }
            xml { render body as XML }
        }
    }

    def update(Long id) {
        if (id == null) {
            response.sendError 405
            //render status: 405
            return
        }

        def post = Post.get(id)
        if (post) {
            //bindData post, params.post
            post.content = params.post.content

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

    def unsupported() {
        response.sendError(405)
    }

    private createErrorList(Errors errors) {
        return errors.allErrors.collect { err ->
            def errorMap = [ message: g.message(error: err).toString() ]
            if (err instanceof FieldError) {
                errorMap["field"] = err.field
                errorMap["value"] = err.rejectedValue
            }
            return errorMap
        }
    }
}
