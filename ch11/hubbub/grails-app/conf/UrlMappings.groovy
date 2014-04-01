class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.${format})?"{
            constraints {
                // apply constraints here
            }
        }

        "/login/form"(controller: "auth", action: "form")

        "/timeline/chuck_norris" {
            controller = "post"
            action = "timeline"
            id = "chuck_norris"
        }
        
        "/timeline" {
            controller = "post"
            action = "personal"
        }

        "/users/$id" {
            controller = "post"
            action = "timeline"
        }

        "/"(view:"/index")
        "500"(view:'/error')
    }
}
