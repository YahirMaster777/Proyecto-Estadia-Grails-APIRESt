package er.model

class UrlMappings {

    static mappings = {
        group "/aplication", {
            post "/create"(controller:'apps', action:'save')
        }

        "/"(controller: 'application', action:'index')
        "500"(view: '/error')
        "404"(view: '/notFound')
    }
}
