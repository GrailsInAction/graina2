package com.grailsinaction

import spock.lang.*
import wslite.http.auth.HTTPBasicAuthorization
import wslite.rest.*

class PostRestFunctionalSpec extends Specification {
    @Shared def restClient =
            new RESTClient("http://localhost:8080/hubbub/api/")

    void setup() {
        restClient.authorization = new HTTPBasicAuthorization("frankie", "testing")
        restClient.httpClient.sslTrustAllCerts = true
    }

    void "GET a list of posts as JSON"() {
        when: "I send a GET to the posts URL requesting JSON"
        def response = restClient.get(path: "/posts", accept: ContentType.JSON)

        then: "I get the expected posts as a JSON list"
        response.json*.message.sort()[0..1] == [
                "Been working my roundhouse kicks.",
                "My first post" ]
    }

    void "GET a list of posts as XML"() {
        when: "I send a GET to the posts URL requesting XML"
        def response = restClient.get(path: "/posts", accept: "application/xml")

        then: "I get the expected posts as an XML document"
        response.xml.post.message*.text().sort()[0..1] == [
                "Been working my roundhouse kicks.",
                "My first post" ]
    }

    void "POST a single post as JSON"() {
        when: "I POST a JSON document to the posts URL"
        def response = restClient.post path: "/posts", accept: ContentType.JSON, {
            type ContentType.JSON
            json message: "A new post!"
        }

        then: "I get a 201 JSON response with the ID of the new post"
        response.statusCode == 201
        response.json.id != null
        response.json.content == "A new post!"
    }

}
