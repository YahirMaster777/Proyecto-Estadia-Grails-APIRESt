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

class CustomRestAuthenticationFailureHandler implements AuthenticationFailureHandler {

    Integer statusCode

    void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.addHeader('WWW-Authenticate', 'X-Auth-Token')
        if (exception instanceof AccountExpiredException) {
            response.setStatus(512)
        } else if (exception instanceof CredentialsExpiredException) {
            response.setStatus(513)
        } else if (exception instanceof DisabledException) {
            response.setStatus(514)
        } else if (exception instanceof LockedException) {
            if (exception.getMessage() == "No Roles found from User"){
                response.setStatus(515)
            }else{
                response.setStatus(517)
            }
        } else if(exception instanceof BadCredentialsException){
            if (exception.getMessage() == "Authentication failed"){
                response.setStatus(518)
            }else if (exception.getMessage() == "Usuario loging block"){
                response.setStatus(519)
            }else{
                response.setStatus(516)
            }
        }else{
            response.setStatus(401)
        }
        response.setContentType("aplication/json")
        response.setCharacterEncoding("UTF-8");
    }
}
