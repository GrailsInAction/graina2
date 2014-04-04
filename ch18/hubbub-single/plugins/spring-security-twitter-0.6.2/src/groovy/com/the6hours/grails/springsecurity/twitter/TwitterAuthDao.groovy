package com.the6hours.grails.springsecurity.twitter

import org.springframework.security.core.GrantedAuthority

/**
 * DAO for Twitter Authentication
 *
 * @since 02.05.11
 * @author Igor Artamonov (http://igorartamonov.com)
 */
public interface TwitterAuthDao<T> {

    /**
     * Try to find existing user for Twitter Token (search by screen name or user id)
     * @param token token for user that has been authenticated by plugin filter
     * @return existing user or null if plugin should create a new one
     */
    T findUser(TwitterAuthToken token)

    /**
     * Create a new user
     *
     * @param token current Authentication Token
     * @return created used
     */
    T create(TwitterAuthToken token)

    /**
     * Make sure that existing user has up-to-date details (like access token or screenname)
     * @param user current user
     * @param token fresh token
     */
    void updateIfNeeded(T user, TwitterAuthToken token)

    /**
     * @param instance of App User related to specified Twitter User (it could be same object / same instance)
     * @return App User
     */
    Object getAppUser(T user)

    /**
     * Principal for Spring Security. Could be any object, but UserDetails instance is preferred
     * @param user
     * @return
     */
    Object getPrincipal(Object user)

    /**
     *
     * @param user current Twitter User
     * @return list of authorities
     */
    Collection<GrantedAuthority> getRoles(T user)
}