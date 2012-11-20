class UrlMappings {
    static mappings = {
        "/basket/$username"(controller: "basket", action: "show")
        "/$controller/$action?/$id?"()
        "500"(view: "error")
    }
}
