import grails.test.GroovyPagesTestCase

/**
 * Integration test case for MyController. Note that testShowNoItems
 * will fail if MyService returns <code>null</code> from its method,
 * unlike the corresponding unit test.
 */
class MyControllerTests extends GroovyTestCase {
    def myService

    void testShowNoItems() {
        def myController = new MyController()
        myController.myService = myService
        myController.params.userId = "glen"

        def model = myController.show()
        assertEquals 0, model["itemNames"].size()
    }

    void testShow() {
        new Item(userId: "glen", name: "chair").save()
        new Item(userId: "peter", name: "desk").save()
        new Item(userId: "glen", name: "table").save(flush: true)

        def myController = new MyController()
        myController.myService = myService
        myController.params.userId = "glen"

        def model = myController.show()
        def names = model["itemNames"]
        assertEquals 2, names.size()
        assertNotNull names.find { it == "chair" }
        assertNotNull names.find { it == "table" }
    }
}
