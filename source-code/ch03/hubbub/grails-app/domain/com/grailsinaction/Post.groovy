package com.grailsinaction

class Post {

    String content
    Date dateCreated

	static belongsTo = [ user : User ] 

    static constraints = {
        content blank: false
    }
    static mapping = {
        sort dateCreated:"desc"              
    }

    static hasMany = [ tags : Tag ]


}
