package com.grailsinaction

/**
 * Test case for {@link PostController}.
 */
class PostControllerUnitTests extends grails.test.ControllerUnitTestCase {
    void testIndex() {
        this.controller.index()

        assertEquals "timeline", redirectArgs["action"]
        assertTrue redirectArgs["params"].isEmpty()
        
        // We need to reset the controller before calling another action
        // on it.
        reset()

        this.controller.params.id = "glen"
        this.controller.index()

        assertEquals "timeline", redirectArgs["action"]
        assertEquals "glen", redirectArgs["params"]["id"]
    }

    void testGlobal() {
        // Test posts returned by our mock PostService. 
        def testPosts = [
                new Post(content: "First post"),
                new Post(content: "Second post") ]

        // Mock the post service.
        def postControl = mockFor(PostService)
        postControl.demand.getGlobalTimelineAndCount() { args ->
            // Check that the controller params are passed to the
            // method.
            assertEquals "glen", args["id"]
            return [ testPosts, testPosts.size() ]
        }

        // Initialise the test controller with out mock service and
        // test parameter data.
        this.controller.postService = postControl.createMock()
        this.controller.params.id = "glen"

        def model = this.controller.global()
        assertEquals testPosts, model["posts"]
        assertEquals testPosts.size(), model["postCount"]
    }

    void testTimeline() {
        // The timeline action picks up the current user from the
        // session.
        mockSession["user"] = [ id: 2L ]
        mockDomain(User, [
                new User(userId: "glen"),
                new User(userId: "chuck") ])

        // Test posts returned by our mock PostService. 
        def testPosts = [
                new Post(content: "First post"),
                new Post(content: "Second post") ]

        // Mock the post service.
        def postControl = mockFor(PostService)
        postControl.demand.getUserTimelineAndCount() { String userId, args ->
            // Check that the controller params are passed to the
            // method.
            assertEquals "chuck", userId
            assertEquals "glen", args["id"]
            return [ testPosts, testPosts.size() ]
        }

        // Initialise the test controller with out mock service and
        // test parameter data.
        this.controller.postService = postControl.createMock()
        this.controller.params.id = "glen"

        def model = this.controller.timeline()
        assertEquals "chuck", model["user"].userId
        assertEquals testPosts, model["posts"]
        assertEquals testPosts.size(), model["postCount"]
    }

    void testPersonal() {
        mockDomain(User, [ new User(userId: "glen"), new User(userId: "peter")])

        // Execute the action
        this.controller.personal()

        // Verify the response contains a 404.
        assertEquals 404, mockResponse.status
    }

    void testPersonalWithId() {
        mockDomain(User, [ new User(userId: "glen"), new User(userId: "peter")])

        // Test posts returned by our mock PostService. 
        def testPosts = [
                new Post(content: "First post"),
                new Post(content: "Second post") ]

        // Mock the post service.
        def postControl = mockFor(PostService)
        postControl.demand.getUserPosts() { String userId, args ->
            // Check that the controller params are passed to the
            // method.
            assertEquals "peter", userId
            assertEquals "peter", args["id"]
            return [ testPosts, testPosts.size() ]
        }

        // Initialise the test controller with out mock service and
        // test parameter data.
        this.controller.postService = postControl.createMock()
        this.controller.params.id = "peter"

        // Execute the action
        def model = this.controller.personal()

        // Verify the model.
        assertEquals "peter", model["user"].userId
        assertEquals testPosts, model["posts"]
        assertEquals testPosts.size(), model["postCount"]
    }
}
