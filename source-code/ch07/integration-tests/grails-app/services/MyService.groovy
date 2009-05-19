class MyService {
    List fetchItemsFor(String userId) {
        // Change the line below to:
        //
        //    return Item.findAllByUserId(userId) ?: null
        //
        // to make the integration test fail.
        return Item.findAllByUserId(userId)
    }
}
