package com.grailsinaction

class GatewayService {

    static expose = ['jabber', 'jms']
    static destination = "jabberOutQ"

    def jmsService

    void onMessage(msg) {
        log.debug "Incoming Queue Request from: ${msg.userId} to: ${msg.to} content: ${msg.content}"
        def addrs = msg.content.split(",")
        addrs.each {addr ->
            log.debug "Sending to: ${addr}"
            sendJabberMessage(addr, msg.content)
        }
    }
    void onJabberMessage(jabber) {
        log.debug "Incoming Jabber Message Received from ${jabber.from()} with body ${jabber.body}"
        def msg = [jabberId: jabber.from, content: jabber.body]
        jmsService.sendQueueJMSMessage ("jabberInQ", msg)
    }
}
