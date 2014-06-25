package com.grailsinaction.security

class AccessControlFilters {
    def grainaSecurityData
    def accessControlService

    def filters = {
        all(controller:'*', action:'*') {
            before = {
                def actions = grainaSecurityData.authRequiredActions[controllerName]

                if (actionName in actions &&
                        !accessControlService.isAuthenticated()) {
                    redirect controller: "login", action: "index"
                }
                else {
                    return true
                }
            }
        }
    }
}
