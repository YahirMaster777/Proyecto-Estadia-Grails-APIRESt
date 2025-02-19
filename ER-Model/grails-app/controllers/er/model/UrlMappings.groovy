package er.model

class UrlMappings {

    static mappings = {
            group "/control", {
                group "/create", {
                    post "/app"(controller:'apps', action: 'create')
                    post "/user"(controller:'users', action: 'create')
                    post "/employee"(controller:'employees', action:'save')
                }
                group "/$uuid", {
                    group "/update",{
                        put "/user"(controller:'users', action: 'update')
                        put "/employee"(controller:'employees', action: 'update')
                    }
                    group "/read", {
                        get "/user"(controller:'users', action: 'read')
                    }
                    group "/delete", {
                        delete "/user"(controller:'users', action: 'delete')
                        delete "/employee"(controller:'employees', action:'delete')
                    }
                    constraints{
                    uuid(matches:/^[0-9a-fA-F]{32}$/)
                    }
                }
                
                group "/list", {
                    get "/user"(controller:'users', action: 'list')
                    group "/all", {
                        get "/user"(controller:'users', action: 'all')
                    }
                }
            
        }
        group "/public", {}

        "/"(controller: 'application', action:'index')
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
