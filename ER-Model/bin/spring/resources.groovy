package com.ordenaris.internalControl

import com.ordenaris.internalControl.MyUserDetailsService
import javax.servlet.http.HttpServletResponse
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.rest.RestAuthenticationFilter
import com.ordenaris.internalControl.UserPasswordEncoderListener
import com.ordenaris.internalControl.CustomRestAuthFilter
import com.ordenaris.internalControl.CustomRestAuthenticationFailureHandler
import com.ordenaris.internalControl.CustomRestAuthenticationSuccessHandler
import com.ordenaris.internalControl.CustomAccessTokenJsonRenderer

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
<<<<<<< HEAD
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
=======
>>>>>>> b3d094ed72e530a5a9a125e6e8f6f1d70ff825f9
}
