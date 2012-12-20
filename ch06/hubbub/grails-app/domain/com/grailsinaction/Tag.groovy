package com.grailsinaction

class Tag {

    String name
    User user

    static hasMany = [ posts : Post ]
    static belongsTo = [ User, Post ]

    static constraints = {
        name blank: false
    }

}
