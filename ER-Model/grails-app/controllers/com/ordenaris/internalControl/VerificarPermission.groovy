package com.ordenaris.internalControl

import grails.artefact.Interceptor
import org.springframework.security.core.context.SecurityContextHolder
import java.lang.SecurityException
import java.io.PrintWriter



class PermisoInterceptor implements Interceptor {
    UsersService usersService

    PermisoInterceptor() {
        matchAll()
    }

    boolean before() {
        def metodo = actionName
        def permisoRequerido = controllerClass?.clazz?.getMethod(metodo)?.getAnnotation(RequierePermiso)?.value()

        println "Verificando permisos para el método: ${metodo} - Requiere permiso: ${permisoRequerido}"
        if (permisoRequerido) {
            def usuario = SecurityContextHolder.context.authentication?.name
            def permisosUsuario = usersService.sections(usuario)

            println "Usuario: ${usuario} - Permisos: ${permisosUsuario}"

            if (permisosUsuario.indexOf(permisoRequerido) < 0) {
                println "Acceso denegado a ${usuario}"
                render(status: 403, text: "Acceso denegado")
                return false
            }
        }


        return true
    }
}
