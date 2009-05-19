class UrlMappingsTests extends grails.test.GrailsUrlMappingsTestCase {
    void testMappings() {
        assertUrlMapping("/item/edit/123",
                controller: "item",
                action: "edit") {
            id = 123
        }

        assertUrlMapping("/basket/fred",
                controller: "basket",
                action: "show") {
            username = "fred"
        }

        // There seems to be a Grails bug preventing this working.
//        assertForwardUrlMapping(500, view: "error")
    }
}
