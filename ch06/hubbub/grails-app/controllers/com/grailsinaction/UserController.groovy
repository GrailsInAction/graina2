package com.grailsinaction

import grails.validation.Validateable

class UserController {
    static scaffold = true

    def search() {}

    def results(String query) {
        def users = User.where { loginId =~ "%${query}%" }.list()
        return [ users: users,
                 term: params.loginId,
                 totalUsers: User.count() ]
    }
}
