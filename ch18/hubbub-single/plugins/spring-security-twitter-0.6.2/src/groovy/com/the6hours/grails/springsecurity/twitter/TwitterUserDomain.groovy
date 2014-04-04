package com.the6hours.grails.springsecurity.twitter

/**
 *
 * If you're upgrading from versions prior to 0.5 - just remove all usages of this interface, remove
 * 'implements TwitterUserDomain' from your domain. You don't need this class anymore.
 *
 * Since version 0.5 you could use standard domain for Twitter Authentication plugin, just
 * add fields: long twitterId, String username, String token, String tokenSecret
 *
 * Btw, if you want to explicitly show that your domain is a domain for Twitter User, you could implement this
 * interface
 */
interface TwitterUserDomain {

    long twitterId
    String username
    String token
    String tokenSecret

}
