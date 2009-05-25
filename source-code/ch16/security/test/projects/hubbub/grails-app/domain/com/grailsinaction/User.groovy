package com.grailsinaction

class User extends com.grailsinaction.security.Account {
    static searchable = true
    
    boolean enabled = true
      
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
        dateCreated()
        profile(nullable: true)
        userRealName(nullable: true, blank: true)
        email(nullable: true, blank: true)
    }


    static mapping = {
        profile lazy:false
    }
}
