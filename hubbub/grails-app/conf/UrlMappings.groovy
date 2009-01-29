class UrlMappings {
    static mappings = {
        "/posts"(controller: "postRest", action: "list")

        "/post"(controller: "postRest") {
            action = [PUT: "save"]
        }

        "/post/$id"(controller: "postRest") {
            action = [GET: "show", POST: "update", DELETE: "delete"]

            constraints {
                id(matches: /\d+/)
            }
        }

        "/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }
        "500"(controller: "errors", action: "internalServer")
    }
}
