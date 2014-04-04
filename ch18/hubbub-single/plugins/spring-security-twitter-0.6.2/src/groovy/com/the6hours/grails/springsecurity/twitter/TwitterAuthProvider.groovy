package com.the6hours.grails.springsecurity.twitter

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.apache.log4j.Logger
import org.springframework.security.core.userdetails.UsernameNotFoundException

/**
 * TODO
 *
 * @since 02.05.11
 * @author Igor Artamonov (http://igorartamonov.com)
 */
class TwitterAuthProvider implements AuthenticationProvider {

    private static final Logger log = Logger.getLogger(this)

    TwitterAuthDao authDao
    boolean createNew = true

    Authentication authenticate(Authentication authentication) {
        TwitterAuthToken token = authentication

        Object user = authDao.findUser(token)

        if (user == null) {
            if (!createNew) {
              throw new UsernameNotFoundException("No user for twitter $token.screenName")
            }
            log.debug "Create new twitter user"
            user = authDao.create(token)
            if (!user) {
               throw new UsernameNotFoundException("Cannot create user for twitter $token.screenName")
            }
        } else {
            authDao.updateIfNeeded(user, token)
        }

        Object appUser = authDao.getAppUser(user)
        Object principal = authDao.getPrincipal(appUser)

        token.details = null
        token.principal = principal
        if (UserDetails.isAssignableFrom(principal.class)) {
            token.authorities = ((UserDetails)principal).getAuthorities()
        } else {
            token.authorities = authDao.getRoles(appUser)
        }
        return token
    }

    boolean supports(Class<? extends Object> authentication) {
        return TwitterAuthToken.isAssignableFrom(authentication)
    }

}
