package com.the6hours.grails.springsecurity.twitter

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.Authentication
import org.springframework.security.authentication.AbstractAuthenticationToken

/**
 * @since 02.05.11
 * @author Igor Artamonov (http://igorartamonov.com)
 */
class TwitterAuthToken extends AbstractAuthenticationToken implements Authentication {

    long userId
    String screenName
    String tokenSecret
    String token

    Collection<GrantedAuthority> authorities
    Object principal

    def TwitterAuthToken() {
        super([] as Collection<GrantedAuthority>)
    }

    public Object getCredentials() {
        return userId
    }

    public Object getPrincipal() {
        return principal
    }
}
