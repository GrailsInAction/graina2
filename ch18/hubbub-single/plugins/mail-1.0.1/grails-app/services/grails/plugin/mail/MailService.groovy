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
package grails.plugin.mail

import org.springframework.mail.MailMessage

/**
 * Provides the entry point to the mail sending API.
 */
class MailService {

    static transactional = false

    def grailsApplication
    def mailMessageBuilderFactory

    MailMessage sendMail(Closure callable) {
        if (isDisabled()) {
            log.warn("Sending emails disabled by configuration option")
            return
        }

        def messageBuilder = mailMessageBuilderFactory.createBuilder(mailConfig)
        callable.delegate = messageBuilder
        callable.resolveStrategy = Closure.DELEGATE_FIRST
        callable.call()

        messageBuilder.sendMessage()
    }

    def getMailConfig() {
        grailsApplication.config.grails.mail
    }

    boolean isDisabled() {
        mailConfig.disabled
    }
}
