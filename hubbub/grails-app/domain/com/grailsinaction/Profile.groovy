package com.grailsinaction

class Profile {

    static belongsTo = User

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
        fullName(nullable: true)
        bio(nullable: true, maxSize: 1000)
        homepage(url: true, nullable: true)
        email(email: true, nullable: true)
        photo(nullable: true)
        country(nullable: true)
        timezone(nullable: true)
        jabberAddress(email: true, nullable: true)
        skin(nullable: true, inList: ['blues', 'nighttime', ''])
    }

    String toString() {
        "Profile for ${fullName} (${id})"
    }

}
