#! /usr/bin/env groovy

// This is the sortPeople() method that will sort a list of Person
// instances by any of its properties.
def sortPeople(people, property) {
    return people.sort { p1, p2 ->
        p1."${property}" <=> p2."${property}"
    }
}

// We'll now create a list of people that we can sort by different
// properties. We use a special syntax supported by Groovy to initialise
// each Person instance.
def people = [
    new Person(firstName: "John", lastName: "Doe", age: 34),
    new Person(firstName: "Chris", lastName: "Jones", age: 18),
    new Person(firstName: "Alice", lastName: "Piper", age: 56),
    new Person(firstName: "Earnest", lastName: "Tindall", age: 54),
    new Person(firstName: "Jane", lastName: "Wells", age: 23),
    new Person(firstName: "Mary", lastName: "Adams", age: 28) ]

// First, sort by first name
people = sortPeople(people, "firstName")
println "Sorted by first name: $people"

// Next by last name
people = sortPeople(people, "lastName")
println "Sorted by last name:  $people"

// Finally by age
people = sortPeople(people, "age")
println "Sorted by age:        $people"


// Groovy allows us to put multiple class declarations in a single file,
// including in scripts!
class Person {
    String firstName
    String lastName
    int age

    String toString() {
        "$firstName $lastName ($age)"
    }
}
