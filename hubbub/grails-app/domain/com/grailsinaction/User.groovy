package com.grailsinaction

class User {
    static searchable = true

    String userId
    String password
    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired

    // For Spring Security plugin's user registration.
    String email
    String userRealName
    boolean emailShow

    Date dateCreated
    Profile profile

    static hasMany = [
            posts : Post,
            tags : Tag,
            following : User ]

    static constraints = {
        userId blank: false, unique: true
        password blank: false
        dateCreated()
        profile(nullable: true)
        userRealName(nullable: true, blank: true)
        email(nullable: true, blank: true)
    }

    static mapping = {
        password column: '`password`'
    }

    Set<Role> getAuthorities() {
        UserRole.findAllByUser(this).collect { it.role } as Set
    }
}
