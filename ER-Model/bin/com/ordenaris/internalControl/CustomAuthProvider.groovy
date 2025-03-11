package com.ordenaris.internalControl

import com.ordenaris.internalControl.*
import java.util.Collection
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.context.ApplicationEventPublisherAware
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.authentication.LockedException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.AccountExpiredException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.CredentialsExpiredException
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.util.Assert
import grails.plugin.springsecurity.userdetails.GrailsUser
import grails.plugin.springsecurity.authentication.encoding.BCryptPasswordEncoder
import grails.util.Holders

class CustomAuthProvider implements AuthenticationProvider {

    def springSecurityService = Holders.grailsApplication.mainContext.getBean('springSecurityService')
    def userService = Holders.grailsApplication.mainContext.getBean('usersService')

    Authentication authenticate(Authentication auth) throws AuthenticationException {
        Assert.isInstanceOf(UserPassOrgAuthToken.class, auth, "Only UserPassOrgAuthToken is supported")
        UserPassOrgAuthToken authentication = (UserPassOrgAuthToken) auth
        return doAuthentication(authentication)
    }

    @Override
    boolean supports(Class authentication) {
        return UserPassOrgAuthToken.class.isAssignableFrom(authentication)
    }

    def fnVerifyStatusUser(user) {
        
        
        if (!user) {
            throw new BadCredentialsException("Account notFound")
        }
        if (!user.enabled) {
            throw new DisabledException("Account disabled")
        }
        if (user.accountExpired) {
            throw new AccountExpiredException("Account Expired")
        }
        if (user.accountLocked) {
            throw new LockedException("Account locked")
        }
        if (user.passwordExpired) {
            throw new CredentialsExpiredException("Credentials Expired")
        }
    }

    def doAuthentication(UserPassOrgAuthToken auth) {
        def respuestaBusqueda = userService.buscarCuenta(auth)
        if (!respuestaBusqueda){
            throw new BadCredentialsException("Account notFound")
        }
        def getUserAuthorities = userService.getUserAuthorities(respuestaBusqueda)
        
        def infoUsers = userService.infoUsers(respuestaBusqueda)

        fnVerifyStatusUser(respuestaBusqueda)
        println infoUsers
        if (respuestaBusqueda) {
            def userDetails = new MyUserDetails(
                respuestaBusqueda.username,
                respuestaBusqueda.password,
                respuestaBusqueda.enabled,
                !respuestaBusqueda.accountExpired,
                !respuestaBusqueda.passwordExpired,
                !respuestaBusqueda.accountLocked,
                getUserAuthorities,
                respuestaBusqueda.id,
                infoUsers
            )
            auth = new UserPassOrgAuthToken(userDetails, auth.credentials, userDetails.authorities, infoUsers)
            return auth
        } else {
            return 
        }
    }
}
