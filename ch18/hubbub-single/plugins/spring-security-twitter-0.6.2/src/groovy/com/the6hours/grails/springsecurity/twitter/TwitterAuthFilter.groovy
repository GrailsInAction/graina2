package com.the6hours.grails.springsecurity.twitter

import grails.plugin.springsecurity.SpringSecurityUtils
import org.codehaus.groovy.grails.web.mapping.LinkGenerator
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.core.Authentication
import org.springframework.social.connect.Connection
import org.springframework.social.oauth1.AuthorizedRequestToken
import org.springframework.social.oauth1.OAuth1Operations
import org.springframework.social.oauth1.OAuth1Parameters
import org.springframework.social.oauth1.OAuthToken
import org.springframework.social.twitter.api.Twitter
import org.springframework.social.twitter.api.UserOperations

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.apache.log4j.Logger
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.social.twitter.connect.*

/**
 * Default Authentication filter
 *
 * @since 09.05.11
 * @author Igor Artamonov (http://igorartamonov.com)
 */
class TwitterAuthFilter extends AbstractAuthenticationProcessingFilter {

    private static def log = Logger.getLogger(this)

    public static final String PREFIX = TwitterAuthFilter.canonicalName
    public static final String REQUEST_TOKEN = PREFIX + ".requestToken"

    String consumerKey
    String consumerSecret

    LinkGenerator linkGenerator

    TwitterAuthFilter(String url) {
        super(url)
    }

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        log.debug "TwitterAuthFilter auth"

        String oauthVerifier = request.getParameter("oauth_verifier")
        if (oauthVerifier == null || oauthVerifier.length() == 0) {
            redirectToTwitter(request, response)
            return null
        }

        try {
            OAuthToken token = verifyToken(request, oauthVerifier)
            TwitterConnectionFactory connectionFactory = new TwitterConnectionFactory(consumerKey, consumerSecret)
            Connection<Twitter> connection = connectionFactory.createConnection(token)
            UserOperations userOperations = connection.api.userOperations()

            TwitterAuthToken securityToken = new TwitterAuthToken(
                    userId: userOperations.profileId,
                    screenName: userOperations.screenName,
                    tokenSecret: token.secret,
                    token: token.value
            )
            securityToken.authenticated = true
            Authentication auth = getAuthenticationManager().authenticate(securityToken)
            if (auth.authenticated) {
                rememberMeServices.loginSuccess(request, response, auth)
                log.info "Successful authentication"
                return auth
            } else {
                throw new DisabledException("User is disabled")
            }
        } catch (Exception e) {
            log.error "Failed processing twitter callback", e
        }
        throw new BadCredentialsException("Invalid twitter token")
    }

    OAuthToken verifyToken(HttpServletRequest request, String oauthVerifier) {
        TwitterServiceProvider provider = new TwitterServiceProvider(consumerKey, consumerSecret)
        OAuth1Operations oauth = provider.getOAuthOperations()

        OAuthToken requestToken = (OAuthToken) request.getSession().getAttribute(REQUEST_TOKEN)

        OAuthToken accessToken = oauth.exchangeForAccessToken(
            new AuthorizedRequestToken(requestToken, oauthVerifier), null);
        request.getSession().removeAttribute(REQUEST_TOKEN)
        return accessToken
    }

    void redirectToTwitter(HttpServletRequest request, HttpServletResponse response) {
        TwitterServiceProvider provider = new TwitterServiceProvider(consumerKey, consumerSecret)
        OAuth1Operations oauth = provider.getOAuthOperations()

        def conf = SpringSecurityUtils.securityConfig.twitter
        String authFilter = conf.filter.processUrl
        String url = linkGenerator.link(uri: authFilter, absolute: true)
        log.debug("Back url: $url")

        OAuthToken requestToken = oauth.fetchRequestToken(url, null)
        request.session.setAttribute(REQUEST_TOKEN, requestToken)
        String authorizeUrl = oauth.buildAuthenticateUrl(requestToken.value, (OAuth1Parameters) OAuth1Parameters.NONE)
        response.sendRedirect(authorizeUrl)
    }

}
