import com.the6hours.grails.springsecurity.twitter.DefaultTwitterAuthDao
import com.the6hours.grails.springsecurity.twitter.TwitterAuthProvider
import com.the6hours.grails.springsecurity.twitter.TwitterAuthFilter
import grails.plugin.springsecurity.SecurityFilterPosition
import grails.plugin.springsecurity.SpringSecurityUtils
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler

/* Copyright 2006-2010 the original author or authors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
class SpringSecurityTwitterGrailsPlugin {

    // the plugin version
    def version = "0.6.2"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.0.0 > *"

    Map dependsOn = ['springSecurityCore': '2.0-RC2 > *']
    def license = 'APACHE'

    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    def issueManagement = [ system: "GitHub", url: "https://github.com/splix/grails-spring-security-twitter/issues" ]
    def scm = [ url: "https://github.com/splix/grails-spring-security-twitter" ]
    def documentation = "http://splix.github.io/grails-spring-security-twitter"

    def observe = ["springSecurityCore"]

    String author = 'Igor Artamonov'
    String authorEmail = 'igor@artamonov.ru'
    String title = 'Twitter Authentication for Spring Security'
    String description = 'Twitter authentication support for the Spring Security plugin.'

    def organization = [ name: "The 6 Hours", url: "http://the6hours.com/" ]

    def doWithSpring = {
        def conf = SpringSecurityUtils.securityConfig
        if (!conf) {
            println 'ERROR: There is no Spring Security configuration'
            println 'ERROR: Stop configuring Spring Security Twitter'
            return
        }

        println 'Configuring Spring Security Twitter ...'

        SpringSecurityUtils.loadSecondaryConfig 'DefaultTwitterSecurityConfig'
        // have to get again after overlaying DefaultTwitterSecurityConfig
        conf = SpringSecurityUtils.securityConfig

        String twitterDaoName = conf?.twitter?.dao ?: null
        boolean _autoCreate = true
        def _autoCreateConf = getConfigValue(conf, 'twitter.autoCreate.active')
        if (_autoCreateConf != null) {
            _autoCreate = _autoCreateConf as Boolean
        }

        if (twitterDaoName == null) {
            twitterDaoName = 'twitterAuthDao'
            List<String> _roles = _autoCreate ? getAsStringList(conf.twitter.autoCreate.roles, 'twitter.autoCreate.roles') : []
            twitterAuthDao(DefaultTwitterAuthDao) {
                twitterUserClassName = conf.twitter.domain.classname
                appUserConnectionPropertyName = conf.twitter.domain.connectionPropertyName

                appUserClassName = conf.userLookup.userDomainClassName
                rolesPropertyName = conf.userLookup.authoritiesPropertyName

                coreUserDetailsService = ref('userDetailsService')
                grailsApplication = ref('grailsApplication')

                defaultRoleNames = _roles
            }
        } else {
            log.info("Using provided Twitter Auth DAO bean: $twitterDaoName")
        }

        String twitterAuthProviderName = getConfigValue(conf, 'twitter.provider', 'twitter.bean.provider')
        if (twitterAuthProviderName != null) {
            log.info("Use provided authentication provider: $twitterAuthProviderName")
            SpringSecurityUtils.registerProvider twitterAuthProviderName
        } else {
            SpringSecurityUtils.registerProvider 'twitterAuthProvider'
            twitterAuthProviderName = 'twitterAuthProvider'
            twitterAuthProvider(TwitterAuthProvider) {
                authDao = ref(twitterDaoName)
                createNew = _autoCreate
            }
        }

        String twitterAuthFilterName = getConfigValue(conf, 'twitter.filter', 'twitter.bean.filter')
        if (twitterAuthFilterName != null) {
            log.info("Use provided authentication filter: $twitterAuthFilterName")
            SpringSecurityUtils.registerFilter twitterAuthFilterName, SecurityFilterPosition.OPENID_FILTER
        } else {
            String _consumerKey = getConfigValue(conf, 'twitter.consumerKey', 'twitter.app.consumerKey')
            String _consumerSecret = getConfigValue(conf, 'twitter.consumerSecret', 'twitter.app.consumerSecret')
            twitterAuthFilterName = 'twitterAuthFilter'
            SpringSecurityUtils.registerFilter 'twitterAuthFilter', SecurityFilterPosition.OPENID_FILTER
            twitterAuthFilter(TwitterAuthFilter, conf.twitter.filter.processUrl) {
                rememberMeServices = ref('rememberMeServices')
                authenticationManager = ref('authenticationManager')
                authenticationDetailsSource = ref('authenticationDetailsSource')
                filterProcessesUrl =  conf.twitter.filter.processUrl
                consumerKey = _consumerKey
                consumerSecret = _consumerSecret
                linkGenerator = ref('grailsLinkGenerator')
                if (conf.twitter.sessionAuthenticationStrategy) {
                    sessionAuthenticationStrategy = ref(conf.twitter.sessionAuthenticationStrategy)
                } else {
                    sessionAuthenticationStrategy = ref('sessionAuthenticationStrategy')
                }
                if (conf.twitter.authenticationFailureHandler) {
                    authenticationFailureHandler = ref(conf.twitter.authenticationFailureHandler)
                } else {
                    authenticationFailureHandler = ref('authenticationFailureHandler')
                }
                if (conf.twitter.authenticationSuccessHandler) {
                    authenticationSuccessHandler = ref(conf.twitter.authenticationSuccessHandler)
                } else if (conf.twitter.popup) {
                    authenticationSuccessHandler = new SimpleUrlAuthenticationSuccessHandler(conf.twitter.filter.processPopupUrl as String)
                } else {
                    authenticationSuccessHandler = ref('authenticationSuccessHandler')
                }
            }
        }
        println "... finished configuring Spring Security Twitter"
    }

    private String getConfigValue(def conf, String ... values) {
        conf = conf.flatten()
        String key = values.find {
            if (!conf.containsKey(it)) {
                return false
            }
            def val = conf.get(it)
            if (val == null || val.toString() == '{}') {
                return false
            }
            return true
        }
        if (key) {
            return conf.get(key)
        }
        return null
    }

    private List<String> getAsStringList(def conf, String paramHumanName) {
        def raw = conf

        if (raw == null) {
            log.error("Invalid $paramHumanName filters configuration: '$raw'")
        } else if (raw instanceof Collection) {
            return raw.collect { it.toString() }
        } else if (raw instanceof String) {
            return raw.split(',').collect { it.trim() }
        } else {
            log.error("Invalid $paramHumanName filters configuration, invalid value type: '${raw.getClass()}'. Value should be defined as a Collection or String (comma separated)")
        }
        return null
    }
}
