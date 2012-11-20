package com.grailsinaction

import org.codehaus.groovy.grails.plugins.springsecurity.RedirectUtils
import org.grails.plugins.springsecurity.service.AuthenticateService

import org.springframework.security.AuthenticationTrustResolverImpl
import org.springframework.security.DisabledException
import org.springframework.security.context.SecurityContextHolder as SCH
import org.springframework.security.ui.AbstractProcessingFilter
import org.springframework.security.ui.webapp.AuthenticationProcessingFilter

/**
 * Login Controller (Example).
 */
class LoginController {

       /**
        * Dependency injection for the authentication service.
        */
       def authenticateService

       /**
        * Dependency injection for OpenIDConsumer.
        */
       def openIDConsumer

       /**
        * Dependency injection for OpenIDAuthenticationProcessingFilter.
        */
       def openIDAuthenticationProcessingFilter

       private final authenticationTrustResolver = new AuthenticationTrustResolverImpl()

       def index = {
               if (isLoggedIn()) {
                       redirect uri: '/'
               }
               else {
                       redirect action: auth, params: params
               }
       }

       /**
        * Show the login page.
        */
       def auth = {

               nocache response

               if (isLoggedIn()) {
                       redirect uri: '/'
                       return
               }

               String view
               String postUrl
               def config = authenticateService.securityConfig.security
               if (config.useOpenId) {
                       view = 'openIdAuth'
                       postUrl = "${request.contextPath}/login/openIdAuthenticate"
               }
               else if (config.useFacebook) {
                       view = 'facebookAuth'
                       postUrl = "${request.contextPath}${config.facebook.filterProcessesUrl}"
               }
               else {
                       view = 'auth'
                       postUrl = "${request.contextPath}${config.filterProcessesUrl}"
               }

               render view: view, model: [postUrl: postUrl]
       }

       /**
        * Form submit action to start an OpenID authentication.
        */
       def openIdAuthenticate = {
               String openID = params['j_username']
               try {
                       String returnToURL = RedirectUtils.buildRedirectUrl(
                                       request, response, openIDAuthenticationProcessingFilter.filterProcessesUrl)
                       String redirectUrl = openIDConsumer.beginConsumption(request, openID, returnToURL)
                       redirect url: redirectUrl
               }
               catch (org.springframework.security.ui.openid.OpenIDConsumerException e) {
                       log.error "Consumer error: $e.message", e
                       redirect url: openIDAuthenticationProcessingFilter.authenticationFailureUrl
               }
       }

       // Login page (function|json) for Ajax access.
       def authAjax = {
               nocache(response)
               //this is example:
               render """
               <script type='text/javascript'>
               (function() {
                       loginForm();
               })();
               </script>
               """
       }

       /**
        * The Ajax success redirect url.
        */
       def ajaxSuccess = {
               nocache(response)
               render '{success: true}'
       }

       /**
        * Show denied page.
        */
       def denied = {
               if (isLoggedIn() && authenticationTrustResolver.isRememberMe(SCH.context?.authentication)) {
                       // have cookie but the page is guarded with IS_AUTHENTICATED_FULLY
                       redirect action: full, params: params
               }
       }

       /**
        * Login page for users with a remember-me cookie but accessing a IS_AUTHENTICATED_FULLY page.
        */
       def full = {
               render view: 'auth', params: params,
                       model: [hasCookie: authenticationTrustResolver.isRememberMe(SCH.context?.authentication)]
       }

       // Denial page (data|view|json) for Ajax access.
       def deniedAjax = {
               //this is example:
               render "{error: 'access denied'}"
       }

       /**
        * login failed
        */
       def authfail = {

               def username = session[AuthenticationProcessingFilter.SPRING_SECURITY_LAST_USERNAME_KEY]
               def msg = ''
               def exception = session[AbstractProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY]
               if (exception) {
                       if (exception instanceof DisabledException) {
                               msg = "[$username] is disabled."
                       }
                       else {
                               msg = "[$username] wrong username/password."
                       }
               }

               if (isAjax()) {
                       render "{error: '${msg}'}"
               }
               else {
                       flash.message = msg
                       redirect action: auth, params: params
               }
       }

       /**
        * Check if logged in.
        */
       private boolean isLoggedIn() {
               return authenticateService.isLoggedIn()
       }

       private boolean isAjax() {
               return authenticateService.isAjax(request)
       }

       /** cache controls */
       private void nocache(response) {
               response.setHeader('Cache-Control', 'no-cache') // HTTP 1.1
               response.addDateHeader('Expires', 0)
               response.setDateHeader('max-age', 0)
               response.setIntHeader ('Expires', -1) //prevents caching at the proxy server
               response.addHeader('cache-Control', 'private') //IE5.x only
       }
}
