class UrlMappings {

    static mappings = {
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

        "/"(view:"/index")
        "500"(view:'/error')
    }
}
