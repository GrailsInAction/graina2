package com.grailsinaction

import org.springframework.security.authentication.event.AuthenticationSuccessEvent

class AuditService {

    static transactional = false

    def springSecurityService

    @grails.events.Listener
    def onNewPost(Post newPost){
        log.error "New Post from: ${newPost.user.loginId} : ${newPost.shortContent}"
        def auditEntry = new AuditEntry(message: "New Post: ${newPost.shortContent}", userId: newPost.user.loginId)
        auditEntry.details = newPost.properties['userId', 'shortContent', 'dateCreated']
        auditEntry.addToTags(new AuditTag(name: "post"))
        auditEntry.addToTags(new AuditTag(name: "create"))
        auditEntry.addToTags(new AuditTag(name: "user-driven"))
        auditEntry.machineName = InetAddress.localHost.hostName
        def dynamicProps = [
                "os-name": System.getProperty("os.name"),
                "os-version": System.getProperty("os.version"),
                "os-java": System.getProperty("java.version")
        ]
        dynamicProps.each { key, value ->
            auditEntry[key] = value
        }
        auditEntry.save(failOnError: true)
    }

    @grails.events.Listener(namespace = 'gorm')
    void onSaveOrUpdate(User user) {
        if (springSecurityService.isLoggedIn()) {
            log.error "Changes made to account ${user?.loginId} by ${springSecurityService?.currentUser}"
        }
    }

    @grails.events.Listener(namespace = 'gorm')
    void onSaveOrUpdate(Post post) {
        if (springSecurityService.isLoggedIn()) {
            log.error "New Post Created: ${post?.content} by ${springSecurityService.currentUser}"
        }
    }

    @grails.events.Listener(namespace = "security")
    def onUserLogin(AuthenticationSuccessEvent loginEvent){
        log.error "We appeared to have logged in a user: ${loginEvent.authentication.principal.username}"
    }


}
