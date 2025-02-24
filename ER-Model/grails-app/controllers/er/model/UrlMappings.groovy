package er.model

class UrlMappings {

    static mappings = {
            group "/admin", {
                group "/create", {
                    post "/user"(controller:'users', action: 'create')
                    post "/employee"(controller:'employees', action:'save')
                    post "/app"(controller:'apps', action: 'create')
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
                        put "/employee"(controller:'employees', action:'delete')
                        delete "/app"(controller:'apps', action:'delete')
                    }
                    group "/activate",{
                       patch "/app"(controller:'apps', action:'active')
                    }
                    constraints {
                        uuid(matches: '^[a-fA-F0-9]{32}$')
                    }
                }
                group "/delete", {
                    delete "/user"(controller:'users', action: 'delete')
                    put "/employee"(controller:'employees', action:'delete')
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
