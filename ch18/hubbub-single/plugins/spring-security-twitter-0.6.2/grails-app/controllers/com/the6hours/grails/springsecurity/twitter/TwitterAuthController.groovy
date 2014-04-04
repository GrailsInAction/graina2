package com.the6hours.grails.springsecurity.twitter

import grails.plugin.springsecurity.SpringSecurityUtils

class TwitterAuthController {

    /**
     * Dependency injection for the springSecurityService.
     */
    def springSecurityService

    def popup = {
        log.debug "Show popup"
        if (springSecurityService.isLoggedIn()) {
            log.debug "Is loggedIn"
            render controller: 'twitterAuth', view: 'popupOk', model: []
        } else {
            log.debug "Not loggedIn"
            def conf = SpringSecurityUtils.securityConfig.twitter

            String authFilter = conf.filter.processUrl
            redirect url: authFilter
        }
    }

    def closePopup = {
        if (springSecurityService.isLoggedIn()) {
            render view: 'popupOk',
                    model: []
        } else {
            render view: 'popupFail',
                    model: []
        }
    }
}
