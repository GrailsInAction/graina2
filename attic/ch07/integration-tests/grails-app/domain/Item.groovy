class Item {
    String userId
    String name

    static constraints = {
        userId(blank: false)
        name(blank: false)
    }
}
