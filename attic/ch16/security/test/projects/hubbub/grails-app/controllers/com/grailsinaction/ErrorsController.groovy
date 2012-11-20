package com.grailsinaction

class ErrorsController {
    def internalServer = {
        render "500 error"
    }

    def notFound = {
        render "Page not found"
    }
}
