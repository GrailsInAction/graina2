#! /usr/bin/env groovy

// Sorting with a closure
fruit = [ "apple", "Orange", "Avocado", "pear", "cherry" ]

fruit.sort { String a, String b ->
    a.compareToIgnoreCase(b)
}

println "Sorted fruit: ${fruit}" 

// Assign a closure to a variable
Closure comparator = { String a, String b ->
    a.compareToIgnoreCase(b)
}

fruit = [ "apple", "Orange", "Avocado", "pear", "cherry" ]

// Pass the closure to the method using the variable we declared
// earlier
fruit.sort(comparator)

println "Sorted fruit (using variable): ${fruit}"

// Call the closure directly, as if it were a method.
assert comparator("banana", "Lemon") < 0
