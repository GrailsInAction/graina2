class JabberGatewayService {

    boolean transactional = true

    static expose = ['jms']

    static destination = "jabberInQ"

    // static listenerCount = 5

    def onMessage = {msg ->
                
        log.error "Got Incoming Jabber Response from: ${msg.jabberId}"

        Profile.withTransaction { tx ->

            try {
                def profile = Profile.findByJabberAddress(msg.jabberId)
                if (profile) {
                    profile.user.addToPosts(new Post(content: msg.content))
                }
            } catch (Throwable t) {
                log.error "Error adding post for ${msg.jabberId}", t
            }
        }

    }


    void sendMessage(post, jabberIds) {

        log.debug "Sending jabber message for ${post.user.userId}..."
        def msg = [userId: post.user.userId,
                content: post.content, to: jabberIds.join(",")]
        sendQueueJMSMessage("jabberOutQ", msg)

    }


}
