import com.grailsinaction.*

class LameSecurityFilters {

    def filters = {

        secureActions(controller:'post', action:'(addPost|deletePost)') {
            before = {

                if (!session.user) {
                    redirect(controller: 'login', action: 'form')
                    return false
                }
                log.debug "Granted access to ${session.user}"

            }
            after = { model->

            }
            afterView = {
                log.debug "Finished running ${controllerName} - ${actionName}"
            }
        }
        

        testingUser(controller:'*', action:'*') {
            before = {
                log.error "Testing Filter is running"
                if (params.login) {
                    session.user = User.findByUserId(params.login)
                    if (session.user) {
                        println "Setting user to ${params.login}"
                    }
                }
                if (!session.user) {
                    
                    def user = User.get(1)
                    if (user) {
                        session.user = user
                        log.error "Setting static user in session to ${user.userId}"
                    } else {
                        log.error "No static user configured - skipping"
                    }
                    
                }
  
            }
        }


    }
}
