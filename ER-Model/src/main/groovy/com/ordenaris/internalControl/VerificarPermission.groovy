class MiInterceptor {

    MiInterceptor() {
        matchAll()
    }

    boolean before() {
        def metodo = actionName
        def controlador = controllerName

        def metodoAnotado = grailsApplication.controllerClasses.find { it.logicalPropertyName == controlador }
            ?.clazz?.methods?.find { it.name == metodo && it.isAnnotationPresent(MiAnotacion) }

        if (metodoAnotado) {
            def anotacion = metodoAnotado.getAnnotation(MiAnotacion)
            println "Valor de la anotación: ${anotacion.valor()}"
            // Aquí puedes agregar la lógica que necesites
        }

        return true
    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
