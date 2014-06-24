package com.grailsinaction

class RestFilters {

    def filters = {
        contentType(controller: "postRest") {
            before = {
                if (!(request.format in ["json", "xml", "all"]) &&
                        !(request.method in ["DELETE", "GET", "HEAD"])) {
                    render status: 415,
                           text: "Unrecognized content type"
                    return false
                }

                if (!(response.format in ["json", "xml", "all"])) {
                    render status: 406,
                           text: "${response.format} not supported"
                    return false
                }
            }
        }
        
    }
}
