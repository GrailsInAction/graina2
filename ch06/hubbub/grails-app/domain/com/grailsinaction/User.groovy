package com.grailsinaction

class User {
    String loginId
    String password
    Date dateCreated

    static hasOne = [ profile: Profile ]
    static hasMany = [ posts: Post, tags: Tag, following: User ]

    static constraints = {
        loginId size: 3..20, unique: true
        password size: 6..8, validator: { passwd, user ->
            return passwd != user.loginId
        }
        
        profile nullable: true
    }

    String toString() { return "User $loginId (id: $id)" }
    String getDisplayString() { return loginId }
}
