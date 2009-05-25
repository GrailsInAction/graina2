package com.grailsinaction.security

class Account {
    String userId
    String password

    static hasMany = [ roles: Role ]

    static constraints = {
        userId(blank: false, size: 3..20, unique: true)
        password(blank: false)
    }
}
