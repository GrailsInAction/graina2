package com.grailsinaction

import spock.lang.*

/**
 * Integration tests that should pass, but the one specifying fetch parameter to list() fails.
 */
class SimpleQueriesIntegrationSpec extends Specification {

    def "Static User list with sorting order"() {
        given: "User domain static list() method is used with parameters sort and order"
        def users = User.list(sort: 'id', order: 'asc')

        when: "a copy of the list is sorted by id"
        def copy = users.collect().sort { it.id }

        then: "both are equals"
        users == copy
    }

    def "Static User list with sorting order and eager fetch"() {
        given: "User domain static list() method is used with parameters sort, order and fetch"
        def users = User.list(sort: 'id', order: 'asc', fetch: [posts: 'eager'])

        when: "a copy of the list is sorted by id"
        def copy = users.collect().sort { it.id }

        then: "both are equals"
        users == copy
    }

    def "Static User list with max"() {
        given: "User domain static list() method used with a max parameter"
        def max = 4
        def users = User.list(max: max)

        when: "We count the results"
        def count = users.size()

        then: "it equals the max given"
        count == max
    }

    def "Static User list with max and eager fetch"() {
        given: "User domain static list() method used with a max parameter and fetch"
        def max = 5
        def users = User.list(max: max, fetch: [posts: 'eager'])

        when: "We count the results"
        def count = users.size()

        then: "it equals the max given"
        count == max
    }

    def "Static User count"() {
        expect: "Static count() method returns correct number of User instances"
        User.count() == 8  // Admin + 7 other users
    }

}
