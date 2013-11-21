package com.grailsinaction

import org.neo4j.graphdb.*

class GraphController {


    private UserGraph getOrCreateMatchingUserGraph(User user) {

        UserGraph matchingGraphUser = UserGraph.findByLoginId(user.loginId)
        if (!matchingGraphUser) {
            matchingGraphUser = new UserGraph(loginId: user.loginId)
            matchingGraphUser.save(failOnError: true)
            if (user.profile?.fullName) {
                matchingGraphUser.fullName = user.profile.fullName
            }
        }
        return matchingGraphUser

    }

    def sync() {

        log.debug("Starting sync process...")

        int syncCount = 0
        int linkCount = 0

        UserGraph.list()*.delete() // go nuclear
        User.list().each { user ->

            UserGraph matchingGraphUser = getOrCreateMatchingUserGraph(user)
            user.following.each { nextFollowing ->
                UserGraph matchingFollow = getOrCreateMatchingUserGraph(nextFollowing)
                matchingGraphUser.addToFollowing(matchingFollow)
                linkCount++
            }
            syncCount++
        }
        render text: "<html>Sync complete. Synced ${syncCount} users with ${linkCount} links at ${new Date()}</html>",
                contentType: "text/html"

    }

    def friendsOfFriends() {

        if (params.id) {
            UserGraph startingUser = UserGraph.findByLoginId(params.id)
            if (startingUser) {
                def resultsTable = startingUser.cypher("start myself=node({this}) MATCH myself-[:following]->friend-[:following]->fof WHERE fof.loginId <> myself.loginId RETURN myself, friend, fof")
                [resultsTable: resultsTable]
            } else {
                response.sendError(404)
            }
        } else {
            response.sendError(404)
        }

    }

    def walk() {

        if (params.id) {
            UserGraph startingUser = UserGraph.findByLoginId(params.id)
            if (startingUser) {
                def followingRel = startingUser.node.relationships.find { it.type.name == 'following' }
                def nodeList = startingUser.traverse(Traverser.Order.BREADTH_FIRST,
                        StopEvaluator.END_OF_GRAPH, ReturnableEvaluator.ALL,
                        followingRel.type, Direction.OUTGOING)
                [nodeList: nodeList]
            } else {
                response.sendError(404)
            }
        } else {
            response.sendError(404)
        }

    }



}
