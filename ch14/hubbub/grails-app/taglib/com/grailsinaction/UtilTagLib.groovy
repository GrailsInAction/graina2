package com.grailsinaction

class UtilTagLib {
    static namespace = "hub"

    def certainBrowser = {  attrs, body ->
        if (request.getHeader('User-Agent') =~ attrs.userAgent ) {
            out << body()
        }
    }
    
    def eachFollower = { attrs, body ->
        def followers = attrs.followers
        followers?.each { follower ->
            body(followUser: follower)
        }
    }
    
    def tinyThumbnail = { attrs ->
        def loginId = attrs.loginId
        out << "<img src='"
        out << g.createLink(action: "tiny", controller: "image", id: loginId)
        out << "' alt='${loginId}'"
    }
    
}
