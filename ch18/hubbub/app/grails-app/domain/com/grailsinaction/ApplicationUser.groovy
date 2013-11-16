package com.grailsinaction

class ApplicationUser {

    String applicationName
    String password
    String apiKey

    static constraints = {

//        importFrom HubbubUser, include: ['password']
        applicationName blank: false, unique: true
        apiKey blank: false

    }
}
