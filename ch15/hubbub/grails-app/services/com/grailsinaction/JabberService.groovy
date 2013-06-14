package com.grailsinaction

class JabberService {

    def jmsService

    void sendMessage(post, jabberIds) {
        log.debug "Sending jabber message for ${post.user.userId}..."
        jmsService.sendQueueJMSMessage("jabberOutQ",
                [ userId: post.user.userId,
                        content: post.content,
                        to: jabberIds.join(",") ] )
    }

}
