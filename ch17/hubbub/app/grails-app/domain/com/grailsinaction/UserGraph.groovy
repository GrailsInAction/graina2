package com.grailsinaction

class UserGraph {

    static mapWith = "neo4j"

    String loginId

    static hasMany = [ following : UserGraph ]

    static constraints = {
        loginId blank: false
    }
}
