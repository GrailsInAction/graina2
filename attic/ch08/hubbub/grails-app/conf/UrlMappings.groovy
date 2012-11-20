class UrlMappings {
    
    static mappings = {

      

        "/timeline/chuck_norris" {
            controller = "post"
            action = "timeline"
            id = "chuck_norris"
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




        "/$controller/$action?/$id?"{
            constraints {
             // apply constraints here
            }
        }


        "500"(view:'/error')
    }
}
