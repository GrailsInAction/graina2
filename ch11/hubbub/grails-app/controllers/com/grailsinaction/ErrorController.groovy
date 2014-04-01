package com.grailsinaction

class ErrorController {

    def internalError() {
        def ex = request.exception.cause

        respond new ErrorDetails(type: ex.getClass().name, message: ex.message), view: "/error"
    }
}

class ErrorDetails {
    String type
    String message
}
