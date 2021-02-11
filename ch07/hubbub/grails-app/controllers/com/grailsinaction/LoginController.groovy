package com.grailsinaction

class LoginController {

    def form(String id) {
        [loginId: id]
    }

    def signIn(String loginId, String password) {
        def user = User.findByLoginId(loginId)
         if(user){
            if(user.password == password){
                session.user = user
                redirect uri: "/"
            }else {
                flash.error = "Unknown username or password"
                redirect action: "form"
            }
        }else {
            flash.error = "Unknown username or password"
            redirect action: "form"
        }
    }
}
