package com.grailsinaction

import grails.converters.*

class PostController {
    def authenticateService
    def postService

    def scaffold = true

    static navigation = [
        [group: 'tabs', action: 'personal', title: 'My Posts', order: 0],
        [action:'timeline', title: 'My Timeline', order: 1],
        [action: 'global', title: 'Everyone', order: 2],

    ]

    def index = {
        redirect(action: 'timeline', params: params)
    }

    def global = {

        def (posts, postCount) = postService.getGlobalTimelineAndCount(params)
        [ posts : posts, postCount : postCount ]
    }

    def timeline = {

        def user = params.id ? User.findByUserId(params.id) : User.get(authenticateService.userDomain().id)
        if (!user) {
            response.sendError(404)
            return
        }
        def (posts, postCount) = postService.getUserTimelineAndCount(user.userId, params)

        [ targetUser : user, posts : posts, postCount : postCount ]
    }

    def personal = {

        def user = params.id ? User.findByUserId(params.id) : User.get(authenticateService.userDomain().id)
        if (!user) {
            response.sendError(404)
            return
        }
        def (posts, postCount) = postService.getUserPosts(user.userId, params)
        
        [ targetUser : user, posts : posts, postCount : postCount ]
    }

    def addPost = {
        try {
            def newPost = postService.createPost(authenticateService.userDomain().userId, params.content)
            flash.message = "Added new post: ${newPost.content}"
        } catch (PostException pe) {
            flash.message = pe.message
        }
        redirect(action: 'timeline', id: params.id)
    }

    def addPostAjax = {
        try {
            def user = authenticateService.userDomain()
            def newPost = postService.createPost(user.userId, params.content)
            def posts
            def postCount
            switch(params.timelineToReturn) {
                case "global":
                    (posts, postCount) = postService.getGlobalTimelineAndCount(params)
                    break
                case "mytimeline":
                    (posts, postCount) = postService.getUserTimelineAndCount(user.userId, params)
                    break
                case "myposts":
                    (posts, postCount) = postService.getUserPosts(user.userId, params)
                    break
            }
            println "postCount is ${postCount}"
            render(template:"postentries", collection: posts, var: 'post')
        } catch (PostException pe) {
            render {
                div(class:"errors", pe.message)
            }
        }

    }

    def tinyurl = {
        def origUrl = params?.fullUrl?.encodeAsURL()
        def tinyUrl = new URL("http://tinyurl.com/api-create.php?url=${origUrl}").text
        render(contentType:"application/json") {
            urls(small: tinyUrl, full: params.fullUrl)
        }
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

}
