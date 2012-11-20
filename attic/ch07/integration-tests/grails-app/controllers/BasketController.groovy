/**
 * Controller required for the URL mapping tests.
 */
class BasketController {
    def show = {
        return [ items: Item.findAllByUserId(params.username), userId: params.username ]
    }
}
