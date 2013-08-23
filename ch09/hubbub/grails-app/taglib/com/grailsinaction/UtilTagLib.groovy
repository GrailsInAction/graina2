package com.grailsinaction

class UtilTagLib {
    static namespace = "hub"

    def certainBrowser = {  attrs, body ->
        if(request.getHeader('User-Agent') =~ attrs.userAgent ) {
            out << body()
        }
    }

    def tinyThumbnail = { attrs ->
        def userId = attrs.loginId
        out << "<img src='"
        out << g.createLink(action: "tiny", controller: "image", id: loginId)
        out << "' alt='${loginId}'"
    }

}
