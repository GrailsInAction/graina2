package com.grailsinaction

class ErrorController {

    def internalError() {
        /*
        try {
        def ex = request.exception.cause ?: request.exception

        respond new ErrorDetails(type: ex.getClass().name, message: ex.message), view: "/error"
        }
        catch (all) {
            all.printStackTrace()
            render "Ugh"
        }
        */
        render "Internal server error"
    }

    def notFound() {
        respond new ErrorDetails(type: "", message: "Page not found"), view: "/error"
    }
}

class ErrorDetails {
    String type
    String message
}
