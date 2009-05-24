class UrlMappings {
    
    static mappings = {
        "/posts"(controller: "postRest") {
            action = [GET: "list", POST: "save"]
        }

        "/post/$id"(controller: "postRest") {
            action = [GET: "show", PUT: "update", DELETE: "delete"]

            constraints {
                id(matches: /\d+/)
            }
        }

        "/$controller/$action?/$id?"{
            constraints {
             // apply constraints here
            }
        }


        "/timeline" {
            controller = "post"
            action = "timeline"
        }

        // alternatively, do it as a one-liner
        // "/timeline/chuck_norris"(controller:"post", action:"timeline", id:"chuck_norris")


        "/users/$id" {
            controller = "post"
            action = "timeline"
        }

        "/"(controller: 'post', action: 'global')

        "/users/$userId/stats" {
            controller = "user"
            action = "stats"
        }

        "/users/$userId/feed/$format?" {
            controller = "user"
            action = "feed"
            constraints {
                format(inList: ['rss', 'atom'])
            }
        }

        "404"(controller: "errors", action: "notFound")
        "500"(controller: "errors", action: "internalServer")
    }
}
