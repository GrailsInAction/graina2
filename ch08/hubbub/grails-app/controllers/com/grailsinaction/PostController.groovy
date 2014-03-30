package com.grailsinaction

class PostController {
    static scaffold = true

    static defaultAction = "home"

    def postService

    def home() {
        if (!params.id) {
            params.id = "chuck_norris"
        }
        redirect(action: 'timeline', params: params)
    }
    
    def timeline(String id) {
        def user = User.findByLoginId(id)
        if (!user) {
            response.sendError(404)
        } else {
            [ user : user ]
        }
    }

    def addPost(String id, String content)  {
        try {
            def newPost = postService.createPost(id, content)
            flash.message = "Added new post: ${newPost.content}"
        } catch (PostException pe) {
            flash.message = pe.message
        }
        redirect(action: 'timeline', id: id)
    }

    def addPostAjax(String content) {
        try {
            def newPost = postService.createPost(session.user.loginId, content)
            def recentPosts = Post.findAllByUser(
                    session.user,
                    [sort: 'dateCreated', order: 'desc', max: 20])
            render template: "postentries", collection: recentPosts, var: 'post'
        } catch (PostException pe) {
            render {
                div(class:"errors", pe.message)
            }
        }
    }
    
}
