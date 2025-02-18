package er.model

class UrlMappings {

    static mappings = {
        group "/private", {
            group "/create", {
                post "/app"(controller:'apps', action: 'create')
                post "/user"(controller:'users', action: 'create')
            }
            group "/$uuid", {
                group "/update",{
                    put "/user"(controller:'users', action: 'update')
                }
                group "/read", {
                    get "/user"(controller:'users', action: 'read')
                }
                group "/delete", {
                    put "/user"(controller:'users', action: 'delete')
                }
                constraints {
                    uuid(matches: '^[a-f0-9]{32}$')
                }
            }
            group "/list", {
                get "/user"(controller:'users', action: 'list')
            }
            group "/all", {
                get "/user"(controller:'users', action: 'all')
            }
            
        }
        group "/public", {}

        "/"(controller: 'application', action:'index')
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
