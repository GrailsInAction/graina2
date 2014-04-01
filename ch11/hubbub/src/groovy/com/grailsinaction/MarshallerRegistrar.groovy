package com.grailsinaction

import grails.converters.*
import java.text.SimpleDateFormat
import javax.annotation.PostConstruct

class MarshallerRegistrar {
    @PostConstruct
    void registerMarshallers() {
        println "Registering custom marshallers"
        def dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")
        JSON.registerObjectMarshaller(Post) { Post p ->
            println "Marshalling..."
            return [ id: p.id,
                    published: dateFormatter.format(p.dateCreated),
                    content: p.content,
                    user: p.user.profile?.fullName,
                    tags: p.tags.collect { it.name } ]
        }

        XML.registerObjectMarshaller(Post) { Post p, converter ->
            converter.build {
                post(id: p.id,
                     published: dateFormatter.format(p.dateCreated)) {
                    content p.content
                    user p.user.profile?.fullName
                    tags {
                        for (t in p.tags) {
                            tag t.name
                        }
                    }
                }
            }
        }

        /*
        // Register an XML marshaller that returns a map rather than uses builder syntax.
        XML.registerObjectMarshaller(Post) { Post p ->
            return [ published: dateFormatter.format(p.dateCreated),
                    content: p.content,
                    user: p.user.profile.fullName,
                    tags: p.tags.collect { it.name } ]
        }
        */
    }
}
