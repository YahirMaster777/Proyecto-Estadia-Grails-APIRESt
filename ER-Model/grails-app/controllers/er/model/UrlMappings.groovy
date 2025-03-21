package er.model

class UrlMappings {

    static mappings = {
        group "/admin", {
            group "/auth", {
                patch "/activate-account/$flag"(controller: 'users', action: 'resetPassword')
            }
            group "/settings", {
                post "/create"(controller:'settings', action:'save')
                get "/refresh"(controller:'settings', action:'refresh')
                group "/$identifier", {
                    patch "/update"(controller:'settings', action:'update')
                    delete "/delete"(controller:'settings', action:'delete')
                }
            }
            group "/app", {
                post "/create"(controller:'apps', action: 'save')
                post "/conections/create"(controller:'appConnections', action:'save')
                get "/all"(controller:'apps', action:'all')
                group "/$uuid", {
                    get "/read"(controller:'apps', action:'info')
                    patch "/$actionService"(controller:'apps', action:'changeStatus')
                    delete "/delete"(controller:'apps', action:'delete')
                    delete "/conection/delete"(controller:'appConnections', action:'delete')
                }
            }
            group "/employee", {
                post "/create"(controller:'employees', action:'save')
                group "/$uuid", {
                    put "/update"(controller:'employees', action: 'update')
                    patch "/$actionService"(controller:'employees', action:'accountManagement')
                    patch "/$status"(controller:'employees', action:'accountManagement')
                    delete "/employee"(controller:'employees', action:'delete')
                }
            }
            group "/profile", {
                post "/create"(controller:'profiles', action:'save')
                post "/create-complete"(controller:'profiles', action:'saveComplete')
                post "/create-permissions"(controller:'profilePermissions', action:'save')
                get "/all"(controller:'profiles', action:'all')
                group "/$uuid", {
                    put "/update"(controller:'profiles', action:'update')
                    get "/read"(controller:'profiles', action:'info')
                    delete "/delete"(controller:'profiles', action:'delete')
                }
            }
            group "/section", {
                post "/create"(controller:'sections', action:'save')
                get "/all"(controller:'sections', action: 'all')
                group "/$uuid", {
                    put "/update"(controller:'sections', action:'update')
                    patch "/$actionService"(controller:'sections', action:'changeStatus')
                    delete "/delete"(controller:'sections', action:'delete')
                }
            }
            group "/user", {
                post "/create"(controller:'users', action: 'create')
                get "/list"(controller:'users', action: 'list')
                get "/all"(controller:'users', action: 'all')
                group "/$uuid", {
                    put "/update"(controller:'users', action: 'update')
                    get "/read"(controller:'users', action: 'read')
                    delete "/delete"(controller:'users', action: 'delete')
                }
            }
            group "/permission", {
                post "/create"(controller:'permissions', action:'save')
                get "/all"(controller:'permissions', action:'all')
                group "$uuid", {
                    get "/read"(controller:'permissions', action:'info')
                    patch "/$actionService"(controller:'permissions', action:'changeStatus')
                    delete "/delete"(controller:'permissions', action:'delete')
                }
            }
            constraints {
                uuid(matches: '^[a-fA-F0-9]{32}$')
            }
        }
        group "/public", {
            group "/auth", {
                post "/recovery-password"(controller: 'recovery', action: 'createToken')
                patch "/$uuid/reset-password"(controller: 'recovery', action: 'resetPassword')
                constraints {
                    uuid(matches: '^[a-fA-F0-9]{32}$')
                }
            }
        }
        "/"(controller: 'application', action:'index')
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}