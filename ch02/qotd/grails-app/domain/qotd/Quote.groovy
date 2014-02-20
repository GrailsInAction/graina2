package qotd

class Quote {
    String content
    String author
    Date created = new Date()

    static constraints = {
        author(blank:false)
        content(maxSize:1000, blank:false)
    }
}
