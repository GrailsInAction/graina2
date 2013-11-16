package com.grailsinaction

class LameSecurityFilters {
    def filters = {
        secureActions(controller: 'post', action: '(addPost|deletePost)') {
            before = {
                if (params.logonId) {
                    session.user = User.findByLoginId(params.logonId)
                }

                if (!session.user) {
                    redirect controller: 'login', action: 'form'
                    return false
                }
            }

            after = { model->
            }

            afterView = {
                log.debug "Finished running ${controllerName} - [CA]${actionName}"
            }
        }
    }
}
