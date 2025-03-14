package er.model

class UrlMappings {

    static mappings = {
        group "/admin", {
            patch "/refresh/setting"(controller:'settings', action:'refresh')
            group "/$identifier", {
                patch "/update/setting"(controller:'settings', action:'update')
                delegate "/delete/setting"(controller:'settings', action:'delete')
            }
            
            group "/create", {
                post "/app"(controller:'apps', action: 'save')
                post "/app-conections"(controller:'appConnections', action:'save')
                post "/employee"(controller:'employees', action:'save')
                post "/profile"(controller:'profiles', action:'save')
                post "/profile-permissions"(controller:'profilePermissions', action:'save')
                post "/section"(controller:'sections', action:'save')
                post "/setting"(controller:'settings', action:'save')
                post "/user"(controller:'users', action: 'create')
                post "/profile-complete"(controller:'profiles', action:'saveComplete')
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
                }
                group "/delete", {
                    delete "/app"(controller:'apps', action:'delete')
                    delete "/conection"(controller:'appConnections', action:'delete')
                    put "/employee"(controller:'employees', action:'delete')
                    delete "/profile"(controller:'profiles', action:'delete')
                    delete "/user"(controller:'users', action: 'delete')
                }
                group "/activate",{
                   patch "/app"(controller:'apps', action:'activate')
                   patch "/profile"(controller:'profiles', action:'activate')
                   patch "/section"(controller:'sections', action:'activate')
                }
                group "/deactivate",{
                    patch "/app"(controller:'apps', action:'deactivate')
                    patch "/profile"(controller:'profiles', action:'deactivate')
                    patch "/section"(controller: 'sections', action:'deactivate')
                }
                constraints {
                    uuid(matches: '^[a-fA-F0-9]{32}$')
                }
            }
            
            group "/list", {
            get "/user"(controller:'users', action: 'list')
            
            }
            group "/all", {
                get "/app"(controller:'apps', action:'all')
                get "/user"(controller:'users', action: 'all')
                get "/profiles"(controller:'profiles', action:'all')
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