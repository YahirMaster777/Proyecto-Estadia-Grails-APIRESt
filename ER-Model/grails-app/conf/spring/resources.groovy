import com.ordenaris.internalControl.UserPasswordEncoderListener
import javax.servlet.http.HttpServletResponse
import com.ordenaris.internalControl.MyUserDetailsService

import com.ordenaris.internalControl.CustomRestAuthenticationFailureHandler
import com.ordenaris.internalControl.CustomRestAuthenticationSuccessHandler
import com.ordenaris.internalControl.CustomAccessTokenJsonRenderer
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.rest.RestAuthenticationFilter
// Place your Spring DSL code here
beans = {
    userPasswordEncoderListener(UserPasswordEncoderListener)

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
}










