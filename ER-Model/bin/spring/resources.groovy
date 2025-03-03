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
}
