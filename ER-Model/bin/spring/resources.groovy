import com.ordenaris.internalControl.UserPasswordEncoderListener
// import com.ordenaris.internalControl.CustomAuthenticationSuccessHandler
// import com.ordenaris.internalControl.CustomAccessTokenJsonRenderer
// import javax.servlet.http.HttpServletResponse
// import com.ordenaris.internalControl.MyUserDetailsService

// import com.ordenaris.internalControl.CustomRestAuthenticationFailureHandler

// import grails.plugin.springsecurity.SpringSecurityUtils
// import grails.plugin.springsecurity.rest.RestAuthenticationFilter
// Place your Spring DSL code here
beans = {
    userPasswordEncoderListener(UserPasswordEncoderListener)

    // userDetailsService(MyUserDetailsService)
    // securityContextRepository(org.springframework.security.web.context.NullSecurityContextRepository)
    // passwordEncoder(CustomPasswordEncoder) {
    //     EncodeHashAsBase64 = false
    // }
    // restAuthenticationFailureHandler(CustomRestAuthenticationFailureHandler){
    //     statusCode = HttpServletResponse.SC_UNAUTHORIZED
    // // }
    // authenticationSuccessHandler(CustomAuthenticationSuccessHandler){
    //     customAccessTokenJsonRendere = customAccessTokenJsonRenderer(CustomAccessTokenJsonRenderer)
    // }
}










