package com.grailsinaction

import grails.converters.JSON

class PostRestControllerUnitTests extends grails.test.ControllerUnitTestCase {
    List testDomains

    protected void setUp() {
        super.setUp()

        testDomains = [
                new Post(content: "First message"),
                new Post(content: "Second message"),
                new Post(content: "Third message"),
                new Post(content: "Fourth message") ]
        mockDomain(Post, testDomains)
    }

    void testListXml() {
        this.controller.request.format = "xml"

        def model = this.controller.list()
        assertEquals 4, model["posts"].size()
        assertEquals testDomains, model["posts"]
    }

    void testListJson() {
        this.controller.request.format = "json"

        this.controller.list()

        // Get the JSON into a form that can be easily tested.
        def objs = JSON.parse(this.controller.response.contentAsString)
        println ">> Objects: $objs"

        // We now have a list of maps. Let's compare to the original
        // domain objects.
        assertEquals 4, objs.size()
        assertEquals testDomains[0].content, objs[0].content
        assertEquals testDomains[1].content, objs[1].content
        assertEquals testDomains[2].content, objs[2].content
        assertEquals testDomains[3].content, objs[3].content
    }

    void testSaveXml() {
        this.controller.request.format = "xml"
        
    }

    void testUpdateXml() {

    }
}
