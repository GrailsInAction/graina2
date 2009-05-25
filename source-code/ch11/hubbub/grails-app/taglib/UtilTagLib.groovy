class UtilTagLib {

    static namespace = "h"

    def lameBrowser = {  attrs, body ->


        if(request.getHeader('User-Agent') =~ attrs.userAgent ) {
            out << body()
        }
    }

    def eachFollower = { attrs, body ->

        def followers = attrs.following
        followers?.each { follower ->
            println "Invoking with ${follower.userId}"
            body(followUser: follower)

        }

    }

    def tinyThumbnail = { attrs ->

        def userId = attrs.userId
        out << "<img src='"
        out << g.createLink(action: "tiny", controller: "image", id: userId)
        out << "' alt='${userId}'"      

    }

}
