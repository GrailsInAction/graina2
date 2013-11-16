package com.grailsinaction

class User {
    transient springSecurityService

    String loginId
    String passwordHash
    boolean enabled
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired
    Date dateCreated

    static hasOne = [ profile : Profile ]

    static constraints = {

        loginId size: 3..20, unique: true, blank: false
        profile nullable: true

    }

    static mapping = {
        profile lazy: false
    }

    Set<Role> getAuthorities() {
        UserRole.findAllByUser(this).collect { it.role } as Set
    }

    def setPassword(String password) {
        this.passwordHash = springSecurityService.encodePassword(password)
    }

    String toString() { return "User $loginId (id: $id)" }
    String getDisplayString() { return loginId }
}
