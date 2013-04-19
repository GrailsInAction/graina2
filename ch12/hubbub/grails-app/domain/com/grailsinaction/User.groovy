package com.grailsinaction

class User {
    transient springSecurityService

    String loginId
    String password
    boolean enabled
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired
    Date dateCreated

    static hasOne = [ profile : Profile ]

    static hasMany = [ posts : Post, tags : Tag, following : User ]

    static constraints = {

        loginId size: 3..20, unique: true, blank: false
        password size: 6..8, blank: false
        tags()
        posts()
        profile nullable: true

    }

    static mapping = {
        profile lazy: false
        password column: '`password`'
    }

    Set<Role> getAuthorities() {
        UserRole.findAllByUser(this).collect { it.role } as Set
    }

    def beforeInsert() {
        encodePassword()
     }
 
    def beforeUpdate() {
        if (isDirty('password')) {
            encodePassword()
        }
    }

    protected void encodePassword() {
        password = springSecurityService.encodePassword(password)
    }

    String toString() { return "User $loginId (id: $id)" }
    String getDisplayString() { return loginId }
}
