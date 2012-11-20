package com.grailsinaction

class User {

    String userId
    String password
      
    Date dateCreated
    Profile profile

    static hasMany = [ posts : Post, tags : Tag, following : User ]
	

    static constraints = {
        userId(size:3..20, unique: true)
        password(size: 6..8, validator: { passwd, user ->
            passwd != user.userId
        })
        dateCreated()
        profile(nullable: true)
    }


    static mapping = {
        profile lazy:false
    }
}
