package com.grailsinaction

class SecurityFilters {
    def filters = {
        // Authentication required for every post action except for
        // "global".
        accessControl(controller: "post", action: '^(?!global).*') {
            before = {
                authcRequired()
            }
        }

        // Add the user to the view model after every action, unless
        // it's already there.
        userInModel(controller: "*", action: "*") {
            after = { model ->
                // Some actions do not have a model. In such cases we
                // don't attempt to add the user.
                if (!model) return

                if (!model["user"] && session["user"]) {
                    model["user"] = User.get(session["user"].id)
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

        // Custom access control to ensure that users can only edit
        // their own profiles, not anybody else's.
        //
        // Because we're using a scaffolded controller for profiles,
        // we do user checks based on the domain instance ID, rather
        // than the username/userId (which is what the example in the
        // book uses).
        profileChanges(controller: "profile", action: "(edit|update)") {
            before = {
                def currUserId = session["user"].id
                if (currUserId != params.id.toLong()) {
                    redirect(controller: "login", action: "unauthorized")
                    return false
                }
                return true
            }
        }
    }
}
