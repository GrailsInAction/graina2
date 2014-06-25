package com.grailsinaction.security

class Account {
    String username
    String passwordHash

    static hasMany = [ roles: Role ]

    static constraints = {
        username blank: false, unique: true
        passwordHash blank: false, bindable: false
    }

    static mapping = {
        table "sec_account"
    }
}
