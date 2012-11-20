/**
 * Unit test case for MyController. Note that these tests pass even
 * when the actual service returns <code>null</code>.
 */
class MyControllerUnitTests extends grails.test.ControllerUnitTestCase {
    void testShowNoItems() {
        def myControl = mockFor(MyService)
        myControl.demand.fetchItemsFor() { String userId ->
            assertEquals "glen", userId
            return []
        }

        this.controller.myService = myControl.createMock()
        this.controller.params.userId = "glen"

        def model = this.controller.show()
        assertEquals 0, model["itemNames"].size()
    }

    void testShow() {
        def testItems = [
                new Item(userId: "glen", name: "chair"),
                new Item(userId: "glen", name: "table") ]

        def myControl = mockFor(MyService)
        myControl.demand.fetchItemsFor() { String userId ->
            assertEquals "glen", userId
            return testItems
        }

        this.controller.myService = myControl.createMock()
        this.controller.params.userId = "glen"

        def model = this.controller.show()
        def names = model["itemNames"]
        assertEquals 2, names.size()
        assertNotNull names.find { it == "chair" }
        assertNotNull names.find { it == "table" }

    }
}
