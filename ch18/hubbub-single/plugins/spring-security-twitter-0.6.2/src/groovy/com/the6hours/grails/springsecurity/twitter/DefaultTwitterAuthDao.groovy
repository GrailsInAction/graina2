package com.the6hours.grails.springsecurity.twitter

import org.apache.log4j.Logger
import org.codehaus.groovy.grails.commons.GrailsApplication
import grails.plugin.springsecurity.SpringSecurityUtils
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.dao.OptimisticLockingFailureException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.GrantedAuthorityImpl
import org.springframework.security.core.userdetails.UserDetails

import java.lang.reflect.Method

/**
 *
 * @author Igor Artamonov (http://igorartamonov.com)
 * @since 21.12.12
 */
class DefaultTwitterAuthDao implements TwitterAuthDao, InitializingBean, ApplicationContextAware, GrailsApplicationAware {

    private static def log = Logger.getLogger(this)

    GrailsApplication grailsApplication
    ApplicationContext applicationContext
    def coreUserDetailsService

    Class TwitterUser
    Class AppUser
    String twitterUserClassName
    String appUserClassName

    String idProperty = "twitterId"
    String usernameProperty = "username"
    String rolesPropertyName

    List<String> defaultRoleNames = ['ROLE_USER', 'ROLE_TWITTER']

    String appUserConnectionPropertyName = "user"

    def twitterAuthService

    Object findUser(TwitterAuthToken token) {
        if (twitterAuthService && twitterAuthService.respondsTo('findUser', token)) {
            return twitterAuthService.findUser(token)
        }
        Object user = null
        TwitterUser.withTransaction {
            user = TwitterUser.findWhere((idProperty): token.userId)
        }
        return user
    }

    void fillTwitterUserDetails(def user, TwitterAuthToken token) {
        user.properties[idProperty] = token.userId
        if (usernameProperty && user.hasProperty(usernameProperty)) {
            user.setProperty(usernameProperty, token.screenName)
        }
        if (user.hasProperty('token')) {
            user.token = token.token
        }
        if (user.hasProperty('tokenSecret')) {
            user.tokenSecret = token.tokenSecret
        }
    }

    void fillAppUserDetails(def appUser, TwitterAuthToken token) {
        def securityConf = SpringSecurityUtils.securityConfig

        String username
        if (twitterAuthService && twitterAuthService.respondsTo('generateUsername', AppUser, TwitterAuthToken)) {
            username = twitterAuthService.generateUsername(appUser, token)
        } else {
            username = token.screenName
        }

        appUser.setProperty(securityConf.userLookup.usernamePropertyName, username)
        appUser.setProperty(securityConf.userLookup.passwordPropertyName, token.tokenSecret)
        appUser.setProperty(securityConf.userLookup.enabledPropertyName, true)
        appUser.setProperty(securityConf.userLookup.accountExpiredPropertyName, false)
        appUser.setProperty(securityConf.userLookup.accountLockedPropertyName, false)
        appUser.setProperty(securityConf.userLookup.passwordExpiredPropertyName, false)
    }

    Object create(TwitterAuthToken token) {
        if (twitterAuthService && twitterAuthService.respondsTo('create', token.class)) {
            return twitterAuthService.create(token)
        }

        def securityConf = SpringSecurityUtils.securityConfig

        def user = null
        def appUser = null

        if (isSameDomain()) {
            if (twitterAuthService && twitterAuthService.respondsTo('createUser', TwitterUser, TwitterAuthToken)) {
                user = twitterAuthService.createUser(user, token)
            } else {
                user = grailsApplication.getDomainClass(TwitterUser.name).newInstance()
                fillTwitterUserDetails(user, token)
                fillAppUserDetails(user, token)
            }
        } else {
            if (twitterAuthService && twitterAuthService.respondsTo('createTwitterUser', TwitterAuthToken)) {
                user = twitterAuthService.createAppUser(token)
            } else {
                user = grailsApplication.getDomainClass(TwitterUser.name).newInstance()
                fillTwitterUserDetails(user, token)
            }            
            if (twitterAuthService && twitterAuthService.respondsTo('createAppUser', TwitterUser, TwitterAuthToken)) {
                appUser = twitterAuthService.createAppUser(user, token)
            } else {
                appUser = grailsApplication.getDomainClass(AppUser.name).newInstance()
                fillAppUserDetails(appUser, token)
            }
            AppUser.withTransaction {
                appUser.save(flush: true, failOnError: true)
            }
            user[appUserConnectionPropertyName] = appUser
        }

        TwitterUser.withTransaction {
            user.save()
        }
        
        if (twitterAuthService && twitterAuthService.respondsTo('afterCreate', TwitterUser, TwitterAuthToken)) {
            twitterAuthService.afterCreate(user, token)
        }

        if (twitterAuthService && twitterAuthService.respondsTo('createRoles', TwitterUser, TwitterAuthToken)) {
            twitterAuthService.createRoles(user, token)
        } else {
            Class<?> PersonRole = grailsApplication.getDomainClass(securityConf.userLookup.authorityJoinClassName).clazz
            Class<?> Authority = grailsApplication.getDomainClass(securityConf.authority.className).clazz
            PersonRole.withTransaction { status ->
                defaultRoleNames.each { String roleName ->
                    String findByField = securityConf.authority.nameField[0].toUpperCase() + securityConf.authority.nameField.substring(1)
                    def auth = Authority."findBy${findByField}"(roleName)
                    if (auth) {
                        PersonRole.create(appUser, auth)
                    } else {
                        log.error("Can't find authority for name '$roleName'")
                    }
                }
            }

        }

        return user
    }

    void updateIfNeeded(Object user, TwitterAuthToken token) {
        if (twitterAuthService && twitterAuthService.respondsTo('updateTokenIfNeeded', user.class, token.class)) {
            twitterAuthService.updateTokenIfNeeded(user, token)
            return
        }
        if (twitterAuthService && twitterAuthService.respondsTo('updateIfNeeded', user.class, token.class)) {
            twitterAuthService.updateIfNeeded(user, token)
            return
        }
        TwitterUser.withTransaction {
            try {
                if (!user.isAttached()) {
                    user.attach()
                }
                boolean update = false
                if (user.hasProperty('token')) {
                    if (user.token != token.token) {
                        update = true
                        user.token = token.token
                    }
                }
                if (user.hasProperty('tokenSecret')) {
                    if (user.tokenSecret != token.tokenSecret) {
                        update = true
                        user.tokenSecret = token.tokenSecret
                    }
                }
                if (user.hasProperty(usernameProperty)) {
                    if (user.getProperty(usernameProperty) != token.screenName) {
                        update = true
                        user.setProperty(usernameProperty, token.screenName)
                    }
                }
                if (update) {
                    user.save()
                }
            } catch (OptimisticLockingFailureException e) {
                log.warn("Seems that user was updated in another thread (${e.message}). Skip")
            } catch (Throwable e) {
                log.error("Can't update user", e)
            }
        }
    }

    Object getAppUser(Object user) {
        if (twitterAuthService && twitterAuthService.respondsTo('getAppUser', user.class)) {
            return twitterAuthService.getAppUser(user)
        }
        if (TwitterUser == AppUser) {
            return user
        }
        def result = null
        AppUser.withTransaction {
            if (!user.isAttached()) {
                user.attach()
            }
            result = user.getProperty(appUserConnectionPropertyName)
        }
        return result
    }

    Object getPrincipal(Object user) {
        if (twitterAuthService && twitterAuthService.respondsTo('getPrincipal', user.class)) {
            return twitterAuthService.getPrincipal(user)
        }
        if (coreUserDetailsService) {
            return coreUserDetailsService.createUserDetails(user, getRoles(user))
        }
        return user
    }

    Collection<GrantedAuthority> getRoles(Object user) {
        if (twitterAuthService && twitterAuthService.respondsTo('getRoles', user.class)) {
            return twitterAuthService.getRoles(user)
        }

        if (UserDetails.isAssignableFrom(user.class)) {
            return ((UserDetails)user).getAuthorities()
        }

        def conf = SpringSecurityUtils.securityConfig
        Class<?> PersonRole = grailsApplication.getDomainClass(conf.userLookup.authorityJoinClassName)?.clazz
        if (!PersonRole) {
            log.error("Can't load roles for user $user. Reason: can't find ${conf.userLookup.authorityJoinClassName} class")
            return []
        }
        Collection roles = []
        PersonRole.withTransaction { status ->
            roles = user?.getAt(rolesPropertyName)
        }
        if (!roles) {
            roles = []
        }
        if (roles.empty) {
            return roles
        }
        return roles.collect {
            if (it instanceof String) {
                return new GrantedAuthorityImpl(it.toString())
            } else {
                new GrantedAuthorityImpl(it.getProperty(conf.authority.nameField))
            }
        }
    }

    /**
     *
     * @return true if app have only one domain for storing both User Details and Twitter Account details
     */
    boolean isSameDomain() {
        return AppUser == TwitterUser
    }

    void afterPropertiesSet() throws Exception {
        log.debug("Init default Twitter Authentication Dao...")
        if (twitterAuthService == null) {
            if (applicationContext.containsBean('twitterAuthService')) {
                log.debug("Use provided twitterAuthService")
                twitterAuthService = applicationContext.getBean('twitterAuthService')
            }
        }
        if (coreUserDetailsService != null) {
            Method m = coreUserDetailsService.class.declaredMethods.find { it.name == 'createUserDetails' }
            if (!m) {
                log.warn("UserDetailsService from spring-security-core don't have method 'createUserDetails()'. Class: ${coreUserDetailsService.getClass()}")
                coreUserDetailsService = null
            } else {
                m.setAccessible(true)
            }
        } else {
            log.warn("No UserDetailsService bean from spring-security-core")
        }

        if (TwitterUser == null) {
            TwitterUser = grailsApplication.getDomainClass(twitterUserClassName)?.clazz
            if (!TwitterUser) {
                log.error("Can't find domain: $twitterUserClassName")
            }
        }
        if (AppUser == null) {
            if (appUserClassName && appUserClassName.length() > 0) {
                AppUser = grailsApplication.getDomainClass(appUserClassName)?.clazz
            }
            if (!AppUser) {
                log.error("Can't find domain: $appUserClassName")
            }
        }
        if (TwitterUser == null && AppUser != null) {
            log.info("Use $AppUser to store Twitter Authentication")
            TwitterUser = AppUser
        } else if (TwitterUser != null && AppUser == null) {
            AppUser = TwitterUser
        }
        log.debug("Twitter Authentication Dao is ready.")
    }
}
