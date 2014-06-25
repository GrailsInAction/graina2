package com.grailsinaction.security

class AccessControlTagLib {
    static namespace = "graina"
    
    def accessControlService

    /**
     * Prints out the full name of a given user.
     * @attr user REQUIRED The user whose full name should be displayed
     * @attr encodeAs The codec to use when encoding the name for the output
     */
    def fullName = { attrs ->
        def codec = attrs.encodeAs ?: "HTML"
        if (codec.equalsIgnoreCase("none")) {
            out << attrs.user.fullName
        }
        else {
            out << attrs.user.fullName."encodeAs$codec"()
        }
    }

    def isLoggedIn = { attrs, body ->
        if (accessControlService.isAuthenticated()) {
            out << body()
        }
    }
}
