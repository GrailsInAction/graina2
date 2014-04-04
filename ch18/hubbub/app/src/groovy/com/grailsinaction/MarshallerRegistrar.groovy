package com.grailsinaction

import grails.converters.*
import java.text.SimpleDateFormat
import javax.annotation.PostConstruct

class MarshallerRegistrar {
    @PostConstruct
    void registerMarshallers() {
        println "Registering custom marshallers"
        def dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")
        JSON.createNamedConfig("v1") { cfg ->
            cfg.registerObjectMarshaller(Post) { Post p ->
                return [ published: dateFormatter.format(p.dateCreated),
                         message: p.content,
                         user: p.user.loginId,
                         tags: p.tags.collect { it.name } ]
            }
        }

        JSON.createNamedConfig("v2") { cfg ->
            cfg.registerObjectMarshaller(Post) { Post p ->
                return [ published: dateFormatter.format(p.dateCreated),
                         message: p.content,
                         user: [id: p.user.loginId,
                                name: p.user.profile.fullName],
                         tags: p.tags.collect { it.name } ]
            }
        }
        

        XML.registerObjectMarshaller(Post) { Post p, converter ->
            converter.attribute "id", p.id.toString()
            converter.attribute "published", dateFormatter.format(p.dateCreated)
            converter.build {
                message p.content
                user p.user.profile?.fullName
                tags {
                    for (t in p.tags) {
                        tag t.name
                    }
                }
            }
        }

        /*
        // Register an XML marshaller that returns a map rather than uses builder syntax.
        XML.registerObjectMarshaller(Post) { Post p ->
            return [ published: dateFormatter.format(p.dateCreated),
                    message: p.content,
                    user: p.user.profile.fullName,
                    tags: p.tags.collect { it.name } ]
        }
        */
    }
}
