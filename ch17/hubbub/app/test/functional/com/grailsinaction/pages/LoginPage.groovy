package com.grailsinaction.pages

class LoginPage extends geb.Page {
    static url = "login/form"

    static content = {
        loginIdField { $("input[name='j_username']") }
        passwordField { $("input[name='j_password']") }
        signInButton { $("input[type='submit']") }
    }

    static at = {
        title.contains("Sign into Hubbub")
    }
}
