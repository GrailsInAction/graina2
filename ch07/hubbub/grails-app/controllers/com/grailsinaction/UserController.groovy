package com.grailsinaction

class UserController {
    static scaffold = true

    def search() {}

    def results(String query) {
        def users = User.where { loginId =~ "%${query}%" }.list()
        return [ users: users,
                 term: params.loginId,
                 totalUsers: User.count() ]
    }

    def advSearch() {}

    def advResults() {
        def profileProps = Profile.metaClass.properties*.name
        def profiles = Profile.withCriteria {
            "${params.queryType}" {

                params.each { field, value ->

                    if (profileProps.grep(field) && value) {
                        ilike(field, value)
                    }
                }

            }

        }
        [ profiles : profiles ]

    }

    def register() {
         if (request.method == "POST") {
            def user = new User(params)
            if (user.validate()) {
                user.save()
                flash.message = "Successfully Created User"
                redirect uri: '/'
            } else {
                flash.message = "Error Registering User"
                return [ user: user ]
            }
         }
    }

    def register2(UserRegistrationCommand urc) {
        if (urc.hasErrors()) {
            return [ user : urc ]
        } else {
            def user = new User(urc.properties)
            user.profile = new Profile(urc.properties)
            if (user.validate() && user.save()) {
                flash.message = "Welcome aboard, ${urc.fullName ?: urc.loginId}"
                redirect uri: '/'
            } else {
                // May not be a unique loginId
                return [ user : urc ]
            }
        }
    }

    def profile(String id) {
        def user = User.findByLoginId(id, [fetch: [profile: "eager"]])
        if (!user) {
            response.sendError(404)
        }
        else {
            [ profile: user.profile ]
        }
    }
    
}

class UserRegistrationCommand {
    String loginId
    String password
    String passwordRepeat
    byte[] photo
    String fullName
    String bio
    String homepage
    String email
    String timezone
    String country
    String jabberAddress

    static constraints = {
        importFrom Profile
        importFrom User
        password(size: 6..8, blank: false,
                 validator: { passwd, urc ->
                    return passwd != urc.loginId
                })
        passwordRepeat(nullable: false,
                validator: { passwd2, urc ->
                    return passwd2 == urc.password
                })
    }
}
