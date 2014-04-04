/*
 * Copyright 2008 the original author or authors.
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

import org.springframework.jndi.JndiObjectFactoryBean
import org.springframework.mail.javamail.JavaMailSenderImpl

import grails.plugin.mail.*

class MailGrailsPlugin {
    def version = "1.0.1"
    def grailsVersion = "1.3 > *"

    def author = "Grails Plugin Collective"
    def authorEmail = "grails.plugin.collective@gmail.com"
    def title = "Provides Mail support to a running Grails application"
    def description = '''\
This plug-in provides a MailService class as well as configuring the necessary beans within
the Spring ApplicationContext.

It also adds a "sendMail" method to all controller classes. A typical example usage is:

sendMail {
    to "fred@g2one.com","ginger@g2one.com"
    from "john@g2one.com"
    cc "marge@g2one.com", "ed@g2one.com"
    bcc "joe@g2one.com"
    subject "Hello John"
    text "this is some text"
}

'''
    def documentation = "http://gpc.github.com/grails-mail/"

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    def license = "APACHE"

    // Details of company behind the plugin (if there is one)
    def organization = [ name: "Grails Plugin Collective", url: "http://github.com/gpc" ]

    // Any additional developers beyond the author specified above.
    def developers = [
        [ name: "Luke Daley", email: "ld@ldaley.com" ],
        [ name: "Peter Ledbrook", email: "pledbrook@vmware.com" ],
        [ name: "Jeff Brown", email: "jbrown@vmware.com" ],
        [ name: "Graeme Rocher", email: "grocher@vmware.com" ],
        [ name: "Marc Palmer", email: "marc@grailsrocks.com" ]
    ]

    // Location of the plugin's issue tracker.
    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPMAIL" ]

    // Online location of the plugin's browseable source code.
    def scm = [ url: "http://github.com/gpc/grails-mail" ]

    def observe = ['controllers','services']

    def mailConfigHash = null
    def mailConfig = null
    def createdSession = false

    def doWithSpring = {
        mailConfig = application.config.grails.mail
        mailConfigHash = mailConfig.hashCode()

        configureMailSender(delegate, mailConfig)

        mailMessageBuilderFactory(MailMessageBuilderFactory) {
            it.autowire = true
        }

        mailMessageContentRenderer(MailMessageContentRenderer) {
            it.autowire = true
        }
    }

    def doWithApplicationContext = { applicationContext ->
        configureSendMail(application, applicationContext)
    }

    def onChange = { event ->
        configureSendMail(event.application, event.ctx)
    }

    def onConfigChange = { event ->
        def newMailConfig = event.source.grails.mail
        def newMailConfigHash = newMailConfig.hashCode()

        if (newMailConfigHash != mailConfigHash) {
            if (createdSession) {
                event.ctx.removeBeanDefinition("mailSession")
            }

            event.ctx.removeBeanDefinition("mailSender")

            mailConfig = newMailConfig
            mailConfigHash = newMailConfigHash

            def newBeans = beans {
                configureMailSender(delegate, mailConfig)
            }

            newBeans.beanDefinitions.each { name, definition ->
                event.ctx.registerBeanDefinition(name, definition)
            }
        }
    }

    def configureMailSender(builder, config) {
        builder.with {
            if (config.jndiName && !springConfig.containsBean("mailSession")) {
                mailSession(JndiObjectFactoryBean) {
                    jndiName = config.jndiName
                }
                createdSession = true
            } else {
                createdSession = false
            }

            mailSender(JavaMailSenderImpl) {
                if (config.host) {
                    host = config.host
                } else if (!config.jndiName) {
                    def envHost = System.getenv()['SMTP_HOST']
                    if (envHost) {
                        host = envHost
                    } else {
                        host = "localhost"
                    }
                }

                if (config.encoding) {
                    defaultEncoding = config.encoding
                } else if (!config.jndiName) {
                    defaultEncoding = "utf-8"
                }

                if (config.jndiName)
                    session = ref('mailSession')
                if (config.port)
                    port = config.port
                if (config.username)
                    username = config.username
                if (config.password)
                    password = config.password
                if (config.protocol)
                    protocol = config.protocol
                if (config.props instanceof Map && config.props)
                    javaMailProperties = config.props
            }
        }
    }

    def configureSendMail(application, applicationContext) {
        //adding sendMail to controllers
        for (controllerClass in application.controllerClasses) {
            controllerClass.metaClass.sendMail = { Closure dsl ->
                applicationContext.mailService.sendMail(dsl)
            }
        }

        def mailServiceClassName = applicationContext.mailService?.metaClass?.theClass?.name

        //adding sendMail to all services, besides the mailService of the plugin
        for (serviceClass in application.serviceClasses) {
            if (serviceClass.clazz.name != mailServiceClassName) {
                serviceClass.metaClass.sendMail = { Closure dsl ->
                    applicationContext.mailService.sendMail(dsl)
                }
            }
        }
    }
}
