package com.grailsinaction

class PostController {
    static scaffold = true

    static navigation = [
        [group: 'tabs', action: 'timeline', title: 'My Timeline', order: 0],
        [action: 'global', title: 'Global Timeline', order: 1]
    ]

    def postService
    def springSecurityService

    def index() {
        if (!params.id) params.id = "chuck_norris"
        redirect action: 'timeline', params: params
    }
    
    def timeline() {
        def user = params.id ? User.findByLoginId(params.id) : springSecurityService.currentUser
        if (!user) {
            response.sendError(404)
        }
        else {
            [ user : user ]
        }
    }

    /**
     * TODO: is this still needed? If so, should it get the ID from the parameter
     * or the current logged in user?
     */
    def addPost(String content) {
        def user = springSecurityService.currentUser
        try {
            def newPost = postService.createPost(user.loginId, content)
            flash.message = "Added new post: ${newPost.content}"
        } catch (PostException pe) {
            flash.message = pe.message
        }
        redirect action: "timeline", id: user.loginId
    }
    
    def addPostAjax(String content) {
        def user = springSecurityService.currentUser
        try {
            def newPost = postService.createPost(user.loginId, content)
            def recentPosts = Post.findAllByUser(session.user, [sort: "dateCreated", order: "desc", max: 20])
            render template: "postentries", collection: recentPosts, var: "post"
        } catch (PostException pe) {
            render {
                div(class:"errors", pe.message)
            }
        }
    }

    def tinyurl(String fullUrl) {
        def origUrl = fullUrl?.encodeAsURL()
        def tinyUrl = new URL("http://tinyurl.com/api-create.php?url=${origUrl}").text
        render(contentType:"application/json") {
            urls(small: tinyUrl, full: params.fullUrl)
        }
    }
    
}
