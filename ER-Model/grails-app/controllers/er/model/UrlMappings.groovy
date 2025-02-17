package er.model

class UrlMappings {

    static mappings = {
        group "/control", {
            group "/user", {
                group"/employee"{
                    
                }
            }
            group"/applicaction",{
            }
            group"/server",{
            }
            
        }
        group "/public", {}

        "/"(controller: 'application', action:'index')
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
