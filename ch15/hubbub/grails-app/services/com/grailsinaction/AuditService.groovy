package com.grailsinaction

import org.springframework.security.authentication.event.AuthenticationSuccessEvent

class AuditService {

    def springSecurityService

    @grails.events.Listener(namespace = "security")
    def onUserLogin(AuthenticationSuccessEvent loginEvent){
        log.error "We appeared to have logged in a user: ${loginEvent.authentication.principal.username}"
    }

    @grails.events.Listener(namespace = 'gorm')
    void onSaveOrUpdate(User user) {
        log.info "Changes made to account- ${user.name} by ${springSecurityService.currentUser}"
    }

    @grails.events.Listener(namespace = 'gorm')
    void onSaveOrUpdate(Post post) {
        log.info "New Post Created: ${post.content} by ${springSecurityService.currentUser}"
    }


}
