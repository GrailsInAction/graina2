package com.grailsinaction

class User {

    String loginId
    String password
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

    static searchable = {
        except = ["password"]
    }

    static mapping = {
        profile lazy: false
    }

    String toString() { return "User $loginId (id: $id)" }
    String getDisplayString() { return loginId }
}
