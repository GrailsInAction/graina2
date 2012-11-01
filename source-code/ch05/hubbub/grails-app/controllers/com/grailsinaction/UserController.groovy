package com.grailsinaction

import grails.validation.Validateable

class UserController {

    def scaffold = true

    def search = {

	}

	def results = {

		def users = User.findAllByUserIdLike("%${params.userId}%")
		return [ users: users, term : params.userId ]

	}

    def advSearch = {
        
    }

    def advResults = {

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

    def register = {

        if (params) {
            def user = new User(params)
            if (user.validate()) {
                user.save()
                flash.message = "Successfully Create User"
                redirect(uri: '/')
            } else {
                flash.message = "Error Registering User"
                return [ user: user ]
            }
            
        }

    }

    def register2 = { UserRegistrationCommand urc ->
        if (urc.hasErrors()) {
            return [ user : urc ]
        } else {
            def user = new User(urc.properties)
            user.profile = new Profile(urc.properties)
            if (user.save()) {
                flash.message = "Welcome aboard, ${urc.fullName ?: urc.userId}"
                redirect(uri: '/')
            } else {
                // maybe not unique userId?
                return [ user : urc ]
            }
        }
    }

    def profile = {
        
        if (params.id) {
            def user = User.findByUserId(params.id)
            if (user) {
                return [ profile : user.profile, userId : user.userId ]
            } else {
                response.sendError(404)
            }
        }

    }

}

@Validateable
class UserRegistrationCommand {

    String userId
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

        // Ensure password does not match userid
        password(size: 6..8, blank: false,
                 validator: { passwd, urc ->
                    return passwd != urc.userId
                })
        passwordRepeat(nullable: false,
                validator: { passwd2, urc ->
                    return passwd2 == urc.password
                })
    }


}

