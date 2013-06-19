package com.grailsinaction

class JabberService {

    static exposes = ["jms"]
    static destination = "jabberInQ"

    static sendQueue = "jabberOutQ"

    def jmsService

    void onMessage(msg) {
        log.debug "Got Incoming Jabber Response from: ${msg.jabberId}"
        try {
            def profile = Profile.findByJabberAddress(msg.jabberId)
            if (profile) {
                profile.user.addToPosts(new Post(content: msg.content))
            }
        } catch (t) {
            log.error "Error adding post for ${msg.jabberId}", t
        }
    }


    void sendMessage(post, jabberIds) {
        log.debug "Sending jabber message for ${post.user.userId}..."
        def msg = [ userId: post.user.userId,
                content: post.content, to: jabberIds.join(",") ]
        jmsService.sendQueueJMSMessage(sendQueue, msg)
    }

}
