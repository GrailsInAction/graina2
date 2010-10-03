import grails.plugins.springsecurity.SecurityConfigType

// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if(System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [ html: ['text/html','application/xhtml+xml'],
                      xml: ['text/xml', 'application/xml'],
                      text: 'text/plain',
                      js: 'text/javascript',
                      rss: 'application/rss+xml',
                      atom: 'application/atom+xml',
                      css: 'text/css',
                      csv: 'text/csv',
                      all: '*/*',
                      json: ['application/json','text/json'],
                      form: 'application/x-www-form-urlencoded',
                      multipartForm: 'multipart/form-data'
                    ]
// The default codec used to encode data with ${}
grails.views.default.codec="none" // none, html, base64
grails.views.gsp.encoding="UTF-8"
grails.converters.encoding="UTF-8"

// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true

// set per-environment serverURL stem for creating absolute links
environments {
    production {
        grails.serverURL = "http://www.changeme.com"
    }
}

grails.mail.host = "localhost"
grails.mail.port = 5566
grails.mail.default.from = "hubbub@grailsinaction.com"


// log4j configuration
log4j = {
    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
	       'org.codehaus.groovy.grails.web.pages', //  GSP
	       'org.codehaus.groovy.grails.web.sitemesh', //  layouts
	       'org.codehaus.groovy.grails."web.mapping.filter', // URL mapping
	       'org.codehaus.groovy.grails."web.mapping', // URL mapping
	       'org.codehaus.groovy.grails.commons', // core / classloading
	       'org.codehaus.groovy.grails.plugins', // plugins
	       'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
	       'org.springframework',
	       'org.hibernate'

    warn   'org.mortbay.log'
}



// Added by the Spring Security Core plugin:
grails.plugins.springsecurity.auth.loginFormUrl = "/"
grails.plugins.springsecurity.failureHandler.defaultFailureUrl = "/"
grails.plugins.springsecurity.authority.className = 'com.grailsinaction.Role'
grails.plugins.springsecurity.securityConfigType = SecurityConfigType.InterceptUrlMap
grails.plugins.springsecurity.successHandler.defaultTargetUrl = "/timeline"
grails.plugins.springsecurity.userLookup.userDomainClassName = 'com.grailsinaction.User'
grails.plugins.springsecurity.userLookup.usernamePropertyName = 'userId'
grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'com.grailsinaction.UserRole'

grails.plugins.springsecurity.interceptUrlMap = [
    '/': [ 'IS_AUTHENTICATED_ANONYMOUSLY' ],
    '/login/**': [ 'IS_AUTHENTICATED_ANONYMOUSLY' ],
    '/captcha/**': [ 'IS_AUTHENTICATED_ANONYMOUSLY' ],
    '/register/**': [ 'IS_AUTHENTICATED_ANONYMOUSLY' ],
    '/js/**': [ 'IS_AUTHENTICATED_ANONYMOUSLY' ],
    '/css/**': [ 'IS_AUTHENTICATED_ANONYMOUSLY' ],
    '/image/**': [ 'IS_AUTHENTICATED_ANONYMOUSLY' ],
    '/images/**': [ 'IS_AUTHENTICATED_ANONYMOUSLY' ],
    '/plugins/**': [ 'IS_AUTHENTICATED_ANONYMOUSLY' ],
    '/post/global': [ 'IS_AUTHENTICATED_ANONYMOUSLY' ],
    '/profile/**': [ 'ROLE_ADMIN' ],
    '/user/list': [ 'ROLE_ADMIN' ],
    '/user/show': [ 'ROLE_ADMIN' ],
    '/user/edit': [ 'ROLE_ADMIN' ],
    '/user/save': [ 'ROLE_ADMIN' ],
    '/user/update': [ 'ROLE_ADMIN' ],
    '/user/create': [ 'ROLE_ADMIN' ],
    '/user/delete': [ 'ROLE_ADMIN' ],
    '/user/**': [ 'IS_AUTHENTICATED_ANONYMOUSLY' ],
    '/**': [ 'IS_AUTHENTICATED_REMEMBERED' ] ]
