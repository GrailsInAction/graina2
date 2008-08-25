#! /usr/bin/env groovy

// Java-style list and map initialisation.
List myList = new ArrayList()
myList.add("apple")
myList.add("orange")
myList.add("lemon")

println "Java list: " + myList

Map myMap = new HashMap()
myMap.put(3, "three")
myMap.put(6, "six")
myMap.put(2, "two")

println "Java map: " + myMap

// Groovy-style
myList = [ "apple", "orange", "lemon" ]
myMap = [ 3: "three", 6: "six", 2: "two" ]

println()
println "Groovy list: " + myList
println "Groovy map: " + myMap

assert myList.size() == 3
assert myMap.size() == 3

// Empty lists and maps
List emptyList = []
Map emptyMap = [:]

assert emptyList.size() == 0
assert emptyMap.size() == 0

// Accessing elements of a list
List numbers = [ 5, 10, 15, 20, 25 ]
assert numbers[0] == 5
assert numbers[3] == 20

assert numbers[-1] == 25
assert numbers[-3] == 15

println()
println "List before the changes: ${numbers}"

numbers[2] = 3
assert numbers[2] == 3

numbers << 30
assert numbers[5] == 30

println "List after the changes:  ${numbers}"

// Accessing elements of a map
Map items = [ "one":   "apple",
              "two":   "orange",
              "three": "pear",
              "four":  "cherry" ]
assert items["two"] == "orange"
assert items["four"] == "cherry"

println "Map before the changes:  ${items}"

items["one"] = "banana"
assert items["one"] == "banana"

items["five"] = "grape"
assert items["five"] == "grape"

println "Map after the changes:   ${items}"

// Sub-lists using a range
List fruit = [
    "apple",
    "pear",
    "lemon",
    "orange",
    "cherry" ]

println()
for (int i in 0..<fruit.size()) {
    println "Fruit number $i is '${fruit[i]}'"
}

// First we try an inclusive range
println "Inclusive range:"

List subList = fruit[1..3]
for (int i in 0..<subList.size()) {
    println "  Sub list item $i is '${subList[i]}'"
}

// Then we try an exclusive range
println "Exclusive range:"
subList = fruit[1..<3]
for (int i in 0..<subList.size()) {
    println "  Sub list item $i is '${subList[i]}'"
}
