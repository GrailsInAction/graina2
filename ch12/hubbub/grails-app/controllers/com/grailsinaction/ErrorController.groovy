package com.grailsinaction

import grails.converters.*

class ErrorController {

    def internalServer() {
        def ex = request.exception
        def body = [class: ex.getClass().name, message: ex.message]

        withFormat {
            html {
                [exception: ex]
            }
            json {
                render body as JSON
            }
            xml {
                render body as XML
            }
        }
    }
}
