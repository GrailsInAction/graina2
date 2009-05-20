class UrlMappings {
    
    static mappings = {

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
