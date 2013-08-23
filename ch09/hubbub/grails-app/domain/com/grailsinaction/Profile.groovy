package com.grailsinaction

class Profile {
    User user
    byte[] photo           
    String fullName
    String bio
    String homepage
    String email
    String timezone
    String country
    String jabberAddress
    String skin

    static constraints = {
        fullName blank: false
        bio nullable: true, maxSize: 1000
        homepage url: true, nullable: true
        email email: true, nullable: false
        photo nullable: true, maxSize: 500 * 1024 // 500Kb
        country nullable: true
        timezone nullable: true
        jabberAddress email: true, nullable: true
        skin nullable: true, blank: true, inList: ['blues', 'nighttime']
    }

    String toString() { return "Profile of $fullName (id: $id)" }
    String getDisplayString() { return fullName }
}
