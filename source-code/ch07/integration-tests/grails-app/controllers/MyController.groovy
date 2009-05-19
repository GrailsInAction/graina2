class MyController {
    def myService

    def show = {
        def items = myService.fetchItemsFor(params.userId)
        
        // This line isn't in the book. However, the ".collect {}"
        // won't throw a NullPointerException, so we use the following
        // line to demonstrate a failure.
        def itemCount = items.size()

        [ itemNames: items.collect { it.name } ]
    }
}
