/**
 * Test case for {@link PostController}.
 */
class PostControllerUnitTests extends grails.test.ControllerUnitTestCase {
    void testShow() {
        mockDomain(User, [ new User(userId: "glen"), new User(userId: "peter")])

        // Show the posts for user "peter".
        this.controller.params.id = "peter"

        // Execute the action
        def model = this.controller.show()

        // Verify the model.
        assertEquals "peter", model["viewUser"]?.userId
    }
}
