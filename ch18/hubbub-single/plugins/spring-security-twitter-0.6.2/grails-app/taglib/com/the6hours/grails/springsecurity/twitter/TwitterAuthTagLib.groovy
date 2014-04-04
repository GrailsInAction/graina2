package com.the6hours.grails.springsecurity.twitter

import grails.plugin.springsecurity.SpringSecurityUtils

/**
 * Twitter Auth tags
 *
 * @since 03.05.11
 * @author Igor Artamonov (http://igorartamonov.com)
 */
class TwitterAuthTagLib {

    static namespace = 'twitterAuth'

	def button = { attrs, body ->
        def conf = SpringSecurityUtils.securityConfig.twitter

        String authFilter = conf.filter.processUrl
        String authUrl = authFilter
        String text = "Connect with Twitter"
        boolean popup = conf.popup

        out << '<span class="twitter-login">'
        out << '<a href="'
        out << createLink(uri: authUrl)
        out << '" class="twitter-button" title="'
        out << text.encodeAsHTML()
        out << '"'
        if (popup) {
            out << 'onclick="twitterConnect(); return false;"'
        }
        out << '>'
        out << '<span>'
        out << text.encodeAsHTML()
        out << '</span></a></span>'

        if (popup) {
            String successUrl = SpringSecurityUtils.securityConfig.successHandler.defaultTargetUrl
            out << '<script type="text/javascript">'
            out << '   function twitterConnect() {'
            out << "     window.open('$authUrl', 'twitter_auth', 'width=640,height=500,toolbar=no,directories=no,status=no,menubar=no,copyhistory=no');"
            out << '   }'
            out << '   function loggedIn() {'
            out << "     window.location.href = '$successUrl';"
            out << '   }'
            out << '</script>'
        }
    }
}
