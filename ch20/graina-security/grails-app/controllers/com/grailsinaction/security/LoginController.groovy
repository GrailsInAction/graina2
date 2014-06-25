package com.grailsinaction.security

class LoginController {
    def accessControlService

    def index() {}

    def signIn = {
        if (!accessControlService.login(
                params.username, params.password)) {
            redirect action: "index"
        }
        else {
            redirect uri: "/"
        }
    }

    def signOut() {
        accessControlService.logout()
        redirect uri: "/"
    }

    def unauthorized() {}
}
