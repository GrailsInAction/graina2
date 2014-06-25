package com.grailsinaction.security

class Role {
    String name

    static constraints = {
        name blank: false, unique: true
    }

    static mapping = {
        table "sec_role"
    }
}
