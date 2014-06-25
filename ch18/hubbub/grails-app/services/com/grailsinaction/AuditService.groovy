package com.grailsinaction

import org.springframework.security.authentication.event.AuthenticationSuccessEvent

class AuditService {

    static transactional = false

    def springSecurityService

    @grails.events.Listener
    def onNewPost(Post newPost){
        log.error "New Post from: ${newPost.user.loginId} : ${newPost.shortContent}"
    }

    @grails.events.Listener(namespace = 'gorm')
    void onSaveOrUpdate(User user) {
        if (springSecurityService.isLoggedIn()) {
            log.error "Changes made to account ${user?.loginId} by ${springSecurityService.currentUser}"
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
