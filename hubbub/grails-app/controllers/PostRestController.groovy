import grails.converters.JSON

class PostRestController {
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
        Post post = new Post(params["post"])

        if (post.save(flush: true)) {
            withFormat {
                xml {
                    render contentType: "application/xml", {
                        // Return the ID of the new post.
                        id(post.id)
                    }
                }
                json {
                    render( [ success: true, id: post.id ] as JSON )
                }
            }
        }
        else {
            response.status = 405
            withFormat {
                xml {
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
                json {
                    def errorList = post.errors.fieldErrors.collect { err ->
                        [ field: err.field, message: g.message(error: err) ]
                    }
                    render errorList as JSON
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
