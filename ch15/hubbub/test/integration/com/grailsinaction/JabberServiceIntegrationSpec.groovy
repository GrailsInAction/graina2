package com.grailsinaction

import grails.plugin.spock.IntegrationSpec
import grails.test.mixin.TestFor
import spock.lang.Specification

class JabberServiceIntegrationSpec extends IntegrationSpec {

    def jabberService

    def "First send to a queue"() {
        given: "Some sample queue data"
        def post = [user: [userId: 'chuck_norris'],
                content: 'is backstroking across the atlantic']
        def jabberIds = ["glen@grailsinaction.com",
                "peter@grailsinaction.com" ]

        when:
        jabberService.sendMessage(post, jabberIds)

        then:
        true

    }
}
