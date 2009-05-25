package com.grailsinaction

class User {
    static searchable = true
    
    String userId
    String password
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
            following : User,
            authorities : Role ]
    static belongsTo = Role
	

    static constraints = {
        userId(blank: false, size:3..20, unique: true)
        password(blank: false)
        dateCreated()
        profile(nullable: true)
        userRealName(nullable: true, blank: true)
        email(nullable: true, blank: true)
    }


    static mapping = {
        profile lazy:false
    }
}
