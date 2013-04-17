package com.grailsinaction

class User {

    String loginId
    String password
    String homepage
    Date dateCreated

    static hasOne = [ profile : Profile ]

    static hasMany = [ posts : Post, tags : Tag, following : User ]

    static constraints = {

        loginId size: 3..20, unique: true
        password size: 6..8
        homepage url: true, nullable: true
        profile nullable: true

    }
}
