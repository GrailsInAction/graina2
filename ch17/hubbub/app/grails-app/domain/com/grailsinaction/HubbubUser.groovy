package com.grailsinaction

class HubbubUser extends User {
    static hasMany = [ posts : Post, tags : Tag, following : User ]
}
