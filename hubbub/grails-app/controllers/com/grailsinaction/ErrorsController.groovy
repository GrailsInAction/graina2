package com.grailsinaction

class ErrorsController {
    def internalServer = {
        render "500 error"
    }
}
