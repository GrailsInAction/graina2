package com.grailsinaction

class SecurityFilters {
    def authenticateService

    def filters = {
        // Add the user to the view model after every action in the
        // post controller, unless it's already there.
        userInModel(controller: "post", action: "*") {
            after = { model ->
                // Some actions do not have a model. In such cases we
                // don't attempt to add the user.
                if (!model) return

                if (!model["user"] && authenticateService.isLoggedIn()) {
                    model["user"] = User.get(authenticateService.userDomain().id)
                }

                // Since the layout requires access to the "following"
                // collection, we make sure that it is fetched here.
                // Otherwise, the layout tries to retrieve the items
                // of the collection and fails because the session is
                // closed.
                if (model["user"]) {
                    model["user"].following.collect { it.userId }
                }
            }
        }

        profileChanges(controller: "profile", action: "edit,update") {
            before = {
                def currUserId = authenticateService.userDomain().userId
                if (currUserId != params.userId) {
                    redirect(controller: "login", action: "denied")
                    return false
                }
                return true
            }
        }
    }
}
