package er.model

class UrlMappings {

    static mappings = {
        group "/admin", {
            group "/create", {
                post "/app"(controller:'apps', action: 'save')
                post "/app-conections"(controller:'appConnections', action:'save')
                post "/employee"(controller:'employees', action:'save')
                post "/profile"(controller:'profiles', action:'save')
                post "/profile-permissions"(controller:'profilePermissions', action:'save')
                post "/section"(controller:'sections', action:'save')
                post "/user"(controller:'users', action: 'create')
                post "/profile-complete"(controller:'profiles', action:'saveComplete')
                post "/permission"(controller:'permissions', action:'save')
            }
            
            group "/$uuid", {
                group "/update",{
                    put "/employee"(controller:'employees', action: 'update')
                    put "/profile"(controller:'profiles', action:'update')
                    put "/user"(controller:'users', action: 'update')
                    put "/section"(controller:'sections', action:'update')
                }
                group "/read", {
                    get "/app"(controller:'apps', action:'info')
                    get "/profile"(controller:'profiles', action:'info')
                    get "/user"(controller:'users', action: 'read')
                    get "/permission"(controller:'permissions', action:'info')
                }
                group "/delete", {
                    delete "/app"(controller:'apps', action:'delete')
                    delete "/conection"(controller:'appConnections', action:'delete')
                    delete "/employee"(controller:'employees', action:'delete')
                    delete "/profile"(controller:'profiles', action:'delete')
                    delete "/permission"(controller:'permissions', action:'delete')
                    delete "/user"(controller:'users', action: 'delete')
                    delete "/section"(controller:'sections', action:'delete')
                }
                constraints {
                    uuid(matches: '^[a-fA-F0-9]{32}$')
                }
                group "/$actionService",{
                    patch "/app"(controller:'apps', action:'changeStatus')
                    patch "/section"(controller:'sections', action:'changeStatus')
                    patch "/permission"(controller:'permissions', action:'changeStatus')
                }
                
            }
            
            group "/list", {
                get "/user"(controller:'users', action: 'list')
            
            }
            group "/all", {
                get "/app"(controller:'apps', action:'all')
                get "/user"(controller:'users', action: 'all')
                get "/permissions"(controller:'permissions', action:'all')
                get "/profiles"(controller:'profiles', action:'all')
                get "/sections"(controller:'sections', action: 'all')
            }
    
        }
        
    group "/public", {
        patch "/$uuid/reset-password"(controller: 'recovery', action: 'resetPassword')
        post "/token"(controller: 'recovery', action: 'createToken')
        // post "/uuid"(controller: 'intentRecovery', action: 'createTkn')
        // put "/recovery-password"(controller:'uuid', action: 'recovery')
        constraints {
            uuid(matches: '^[a-fA-F0-9]{32}$')
        }
    }
    get "/api/login"(controller:'recovery', action: 'activateAccount')
    "/"(controller: 'application', action:'index')
    "500"(view: '/error')
    "404"(view: '/notFound')
}
}