import com.ordenaris.internalControl.UserPasswordEncoderListener
import javax.servlet.http.HttpServletResponse
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.rest.RestAuthenticationFilter
// Place your Spring DSL code here
beans = {
    userPasswordEncoderListener(UserPasswordEncoderListener)
}


package com.ordenaris.distribuidores
import com.ordenaris.distribuidores.MyUserDetailsService

import com.ordenaris.distribuidores.UserPasswordEncoderListener
import com.ordenaris.distribuidores.CustomRestAuthFilter
import com.ordenaris.distribuidores.CustomRestAuthenticationFailureHandler
import com.ordenaris.distribuidores.CustomRestAuthenticationSuccessHandler
import com.ordenaris.distribuidores.CustomAccessTokenJsonRenderer
beans = {
    userDetailsService(MyUserDetailsService)
    securityContextRepository(org.springframework.security.web.context.NullSecurityContextRepository)
    passwordEncoder(CustomPasswordEncoder) {
        EncodeHashAsBase64 = false
    }
    restAuthenticationFailureHandler(CustomRestAuthenticationFailureHandler){
        statusCode = HttpServletResponse.SC_UNAUTHORIZED
    }
    restAuthenticationSuccessHandler(CustomRestAuthenticationSuccessHandler){
        customAccessTokenJsonRendere = customAccessTokenJsonRenderer(CustomAccessTokenJsonRenderer)
    }
    restAuthenticationFilter(CustomRestAuthFilter)
}










