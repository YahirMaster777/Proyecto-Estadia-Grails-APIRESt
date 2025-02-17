package er.model

class UrlMappings {

    static mappings = {
        group "/admin", {
            group "/user", {
                post "/create"(controller: 'user', action:'manage' )
                group "/$username", {
                    put "/update"(controller: 'user', action:'update')
                }
            }
        }
        group "/public", {}

        "/"(controller: 'application', action:'index')
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
