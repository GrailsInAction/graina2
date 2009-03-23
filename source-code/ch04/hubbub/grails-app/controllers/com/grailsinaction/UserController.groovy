package com.grailsinaction

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

}
