// package com.ordenaris.distribuidores

// import grails.plugin.springsecurity.rest.token.AccessToken
// import groovy.transform.CompileStatic
// import org.springframework.security.core.Authentication
// import org.springframework.security.web.authentication.AuthenticationSuccessHandler

// import javax.servlet.ServletException
// import javax.servlet.http.HttpServletRequest
// import javax.servlet.http.HttpServletResponse

// @CompileStatic
// class CustomRestAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

//     void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//         CustomAccessTokenJsonRenderer customAccessTokenJsonRendere = new CustomAccessTokenJsonRenderer()
//         response.contentType = 'application/json'
//         response.characterEncoding = 'UTF-8'
//         response.addHeader 'Cache-Control', 'no-store'
//         response.addHeader 'Pragma', 'no-cache'
//         response << customAccessTokenJsonRendere.generateJson( authentication as AccessToken )
//     }

// }
