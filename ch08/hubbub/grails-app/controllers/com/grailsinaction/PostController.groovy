package com.grailsinaction

class PostController {
    static scaffold = true

    static navigation = [
        [group: 'tabs', action: 'timeline', title: 'My Timeline', order: 0],
        [action: 'global', title: 'Global Timeline', order: 1]
    ]

    def postService

    def index() {
        if (!params.id) params.id = "chuck_norris"
        redirect action: 'timeline', params: params
    }
    
    def timeline() {
        def user = User.findByLoginId(params.id)
        if (!user) {
            response.sendError(404)
        } else {
            [ user : user ]
        }
    }

    def addPost(String id, String content) {
        try {
            def newPost = postService.createPost(id, content)
            flash.message = "Added new post: ${newPost.content}"
        } catch (PostException pe) {
            flash.message = pe.message
        }
        redirect action: 'timeline', id: id
    }
    
}
