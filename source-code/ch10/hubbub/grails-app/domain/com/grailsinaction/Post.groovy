package com.grailsinaction

class Post {

    static searchable = {
        user(component:true)
        spellCheck "include"
    }

    String content
    Date dateCreated

    static constraints = {
        content(blank: false)
    }

    static belongsTo = [ user : User ]

    static mapping = {
        sort dateCreated:"desc"
    }

    static hasMany = [ tags : Tag ]


}
