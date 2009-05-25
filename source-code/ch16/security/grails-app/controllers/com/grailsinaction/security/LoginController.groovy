package com.grailsinaction.security

class LoginController {
    def passwordHandler

    def index = {}

    def signIn = {
        // Check that the given user exists and that the password is
        // correct.
        def user = Account.findByUserId(params.userId)
        if (!user || !passwordHandler.passwordsMatch(params.password, user.password)) {
            flash.message = "Invalid username or password."
            redirect(action: "index")
        }

        // User has successfully authenticated, so add the instance to
        // the session.
        session.user = user

        // We'll be lazy and simply redirect back to the home page.
        // Ideally, we would redirect back to the page that the user
        // originally navigated to.
        redirect(uri: "/")
    }

    def signOut = {
        // Remove the user from the session.
        session.user = null

        // Redirect back to home again.
        redirect(uri: "/")
    }

    def unauthorized = {
        render "You are not authorized to access this page."
    }
}
