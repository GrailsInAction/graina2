package com.grailsinaction

import org.bson.types.ObjectId

class AuditEntry {

    static mapWith = "mongo"

    ObjectId id
    String message
    String userId
    Date dateCreated

    Map details

    static hasMany = [ tags : AuditTag ]

    static embedded = ['tags']

    static constraints = {
        message blank: false
        userId blank: false
    }

    static mapping = {
        collection "logs"
        database "audit"
        userId index:true
        version false
    }

}
