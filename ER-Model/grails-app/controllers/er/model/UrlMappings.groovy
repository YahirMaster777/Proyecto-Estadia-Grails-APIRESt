package er.model

class UrlMappings {

    static mappings = {
        group "/admin", {
            group "/create", {
                post "/app"(controller:'apps', action: 'save')
                post "/user"(controller:'users', action: 'create')
                post "/employee"(controller:'employees', action:'save')
                post "/profile"(controller:'profiles', action:'save')
                post "/profile-permissions"(controller:'profilePermissions', action:'save')
                post "/app-conections"(controller:'appConnections', action:'save')
                post "/section"(controller:'sections', action:'save')
            }
            
            group "/$uuid", {
                group "/update",{
                    put "/user"(controller:'users', action: 'update')
                    put "/employee"(controller:'employees', action: 'update')
                    put "/profile"(controller:'profiles', action:'update')
                }
                group "/read", {
                    get "/user"(controller:'users', action: 'read')
                    get "/profile"(controller:'profiles', action:'info')
                    get "/app"(controller:'apps', action:'info')
                }
                group "/delete", {
                    delete "/user"(controller:'users', action: 'delete')
                    put "/employee"(controller:'employees', action:'delete')
                    delete "/app"(controller:'apps', action:'delete')
                    delete "/profile"(controller:'profiles', action:'delete')
                }
                group "/activate",{
                   patch "/app"(controller:'apps', action:'activate')
                   patch "/section"(controller:'sections', action:'activate')
                }
                group "/deactivate",{
                    patch "/app"(controller:'apps', action:'deactivate')
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
                get "/user"(controller:'users', action: 'all')
                get "/app"(controller:'apps', action:'all')
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