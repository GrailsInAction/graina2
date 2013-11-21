package org.example

class Book {
    String title
    String author

    static constraints = {
        title blank: false
        author blank: false
    }
}
