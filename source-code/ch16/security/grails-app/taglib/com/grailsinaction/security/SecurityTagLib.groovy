package com.grailsinaction.security

class SecurityTagLib {
    static namespace = "sec"

    def isLoggedIn = { attrs, body ->
        if (session["user"] != null) {
            out << body()
        }
    }

    def isNotLoggedIn = { attrs, body ->
        if (session["user"] == null) {
            out << body()
        }
    }

    def userId = {
        out << session["user"]?.userId
    }
}
