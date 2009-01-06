class PostRestController {
    def list = {
        [ posts: Post.list() ]
    }

    def show = {
        def post = Post.get(params.id)
        if (post) {
            return [ post: post ]
        }
        else {
            response.sendError(404)
        }
    }

    def save = {
        Post post = new Post(params["post"])

        if (post.save(flush: true)) {
            render contentType: "application/xml", {
                // Return the ID of the new post.
                id(post.id)
            }
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
