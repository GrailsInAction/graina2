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
    
}

class PostDetails {
    String message

    static constraints = {
        message blank: false, nullable: false
    }
}
