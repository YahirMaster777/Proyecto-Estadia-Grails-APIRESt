package com.ordenaris.internalControl

import grails.plugin.springsecurity.SpringSecurityService
import groovy.json.*
import javax.servlet.http.HttpServletResponse
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.authentication.LockedException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.AccountExpiredException
import org.springframework.security.authentication.CredentialsExpiredException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
//libreria para imprimir mensaje de error
import java.io.PrintWriter

class CustomRestAuthenticationFailureHandler implements AuthenticationFailureHandler {

    Integer statusCode
    
    

    void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.addHeader('WWW-Authenticate', 'X-Auth-Token')
        println("Custom rest Auth")
        def mensaje
        if (exception instanceof AccountExpiredException) {
            mensaje =  "La cuenta expiro"
            response.setStatus(512)
        } else if (exception instanceof CredentialsExpiredException) {
            mensaje = "Las Credenciales Expiraron"
            response.setStatus(513)
        } else if (exception instanceof DisabledException) {
            mensaje = "La cuenta esta inhabilitada"
            response.setStatus(514)
        } else if (exception instanceof LockedException) {
            if (exception.getMessage() == "No Roles found from User"){
                mensaje = "El usuario no tiene un rol"
                response.setStatus(515)
            }else{
                mensaje = "Error al iniciar sesion"
                response.setStatus(517)
            }
        } else if(exception instanceof BadCredentialsException){
            if (exception.getMessage() == "Authentication failed"){
                mensaje = "Error Introduzca otras crendeciales"
                response.setStatus(518)
                
            }else if (exception.getMessage() == "Usuario loging block"){
                mensaje = "Cuenta Bloqueada"
                response.setStatus(519)
            }else{
                mensaje = "Error al Iniciar sesion"
                response.setStatus(516)
            }
        }else{
            mensaje =  "Sin Autorizacion"
            response.setStatus(401)
        }
        response.setContentType("aplication/json")
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        HashMap resp = [ success:false, mensaje: mensaje]
        out.println(resp.toPrettyString());
    }
}
