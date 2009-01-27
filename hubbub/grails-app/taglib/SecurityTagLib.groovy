class SecurityTagLib {
    def isLoggedIn = { attrs, body ->
        if (session.user) {
            out << body()
        }
    }
}
