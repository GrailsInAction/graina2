package org.codehaus.groovy.grails.jabber

import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.PacketListener
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.filter.PacketFilter
import org.jivesoftware.smack.filter.PacketTypeFilter
import org.jivesoftware.smack.packet.Message

import org.apache.log4j.Logger

/**
 * A simple chat bot service using Jabber.  Based on http://memo.feedlr.com/?p=11 and
 * http://blogs.bytecode.com.au/glen/2008/01/03/gravl--google-talk-notifier.html
 *
 * @author Glen Smith
 *
 */
class ChatListener {

    private static final Logger log = Logger.getLogger(ChatListener.class)

    XMPPConnection connection

    def host
    def port = 5222
    def serviceName = "XMPP"

    def userName
    def password

    def listenerMethod
    def targetService

    def connect = {


        ConnectionConfiguration cc = new ConnectionConfiguration(host,
            port, serviceName)

        connection = new XMPPConnection(cc)



        try {

            log.error "Connecting to Jabber server"
            connection.connect()
            connection.login(userName, password, userName + Long.toHexString(System.currentTimeMillis()))
           log.error "Connected to Jabber server: ${connection.isConnected()}"

        } catch (Exception e) {

            log.error "Jabber Connection failed: $e.message", e

        }

    }

    def listen = { 

        if (!connection)
            connect()
            //throw new RuntimeException("Must have an active connection before adding chat listener")

        PacketFilter msgFilter = new PacketTypeFilter(Message.class)

        def myListener = [processPacket: { packet ->

            log.error "Received message from ${packet.from}, subject: ${packet.subject}, body: ${packet.body}"
            // callback(packet)

            targetService[listenerMethod].call(packet)

        }] as PacketListener

        log.debug "Adding Jabber listnener..."
        connection.addPacketListener(myListener, msgFilter)


    }



    def disconnect() {

        if (connection && connection.isConnected())
            connection.disconnect()

    }



    def sendJabberMessage(String to, String msg) {

        try {

            if (!connection)
                connect()

            Chat chat = connection.chatManager.createChat(to, null)
            def msgObj = new Message(to, Message.Type.chat)
            msgObj.setBody(msg)

            log.debug "Sending Jabber message to ${to} with content ${msg}"
            chat.sendMessage(msgObj)


        } catch (Exception e) {

            log.error "Failed to send Jabber message", e

        }

    }


}