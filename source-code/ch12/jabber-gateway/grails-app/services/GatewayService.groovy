class GatewayService {

    static expose = ['jabber', 'jms']

    static destination = "jabberOutQ"

    def onMessage = { msg ->

        log.info "Incoming Queue Request from: ${msg.userId} to: ${msg.to} content: ${msg.content}"

        def addrs = msg.content.split(",")
        addrs.each { addr ->
            log.debug "Sending to: ${addr}"
            sendJabberMessage(addr, msg.content)
        }

    }

    def onJabberMessage = { jabber ->

        log.info "Incoming Jabber Message Received from: ${jabber.from} with body ${jabber.body}"
		def jabberClientsDeets = jabber.from.split("/")
        def msg = [ jabberId: jabberClientsDeets[0], content: jabber.body, 
						jabberClient: jabberClientsDeets[1] ]
		log.debug "About to send: ${msg.dump()}"
        sendQueueJMSMessage("jabberInQ", msg)
        
    }


}
