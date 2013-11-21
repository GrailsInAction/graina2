class UrlMappings {

    static mappings = {
        def args = [controller: "postRest", parseRequest: true]
        def c = {
            action = [GET: "list", POST: "save", PUT: "unsupported", DELETE: "unsupported"]
        }
        "/api/v${v}/posts" args, c
        "/api/posts" args, c

        "/api/posts/$id" controller: "postRest", parseRequest: true, {
            action = [GET: "show", POST: "unsupported", PUT: "update", DELETE: "delete"]
        }

        "/timeline/chuck_norris" {
            controller = "post"
            action = "timeline"
            id = "chuck_norris"
        }

        "/users/$id" {
            controller = "post"
            action = "timeline"
        }
    
        "/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }

        "/" view:"/index"
        "500" controller: "error", action: "internalServer"
    }
}
