package com.ordenaris.internalControl

import com.ordenaris.internalControl.*

import java.util.Collection;

import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.aop.aspectj.RuntimeTestWalker.ThisInstanceOfResidueTestVisitor
import org.springframework.context.ApplicationEventPublisherAware
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.authentication.LockedException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.AccountExpiredException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.util.Assert
import grails.plugin.springsecurity.userdetails.GrailsUser
import grails.plugin.springsecurity.authentication.encoding.BCryptPasswordEncoder

import grails.util.Holders

class CustomAuthProvider implements AuthenticationProvider{

	def springSecurityService = Holders.grailsApplication.mainContext.getBean('springSecurityService')
	def userService = Holders.grailsApplication.mainContext.getBean('userService')

	Authentication authenticate(Authentication auth) throws AuthenticationException{
		Assert.isInstanceOf(UserPassOrgAuthToken.class, auth, "Only UserPassOrgAuthToken is supported")
		UserPassOrgAuthToken authentication = (UserPassOrgAuthToken) auth
		return doAuthentication(authentication)
	}

	@Override
	boolean supports(Class authentication){
		return UserPassOrgAuthToken.class.isAssignableFrom(authentication)
	}

	def fnVerifyStatusUser( user ){
		if (!user.enabled){
			throw new DisabledException("Account disabled")
		}
		if (user.accountExpired){
			throw new AccountExpiredException("Account expired")
		}
		if (user.accountLocked){
			throw new LockedException("Account locked")
		}
	}

    // our custom authorization logic
    def doAuthentication(UserPassOrgAuthToken auth){

      def respuestaBusqueda = userService.buscarCuenta( auth )
      if( respuestaBusqueda.success ){
        fnVerifyStatusUser( respuestaBusqueda.user )

        def idDistribuidor = 0
        if( respuestaBusqueda.distribuidor ){
          idDistribuidor = respuestaBusqueda.distribuidor.id
        }

        userService.registrarActividad( respuestaBusqueda.user )

        def userDetails = new MyUserDetails(
          respuestaBusqueda.user.username,
          respuestaBusqueda.user.crd,
          respuestaBusqueda.user.enabled,
          !respuestaBusqueda.user.accountExpired,
          !respuestaBusqueda.user.crdExpired,
          !respuestaBusqueda.user.accountLocked,
          respuestaBusqueda.autorities,          
          respuestaBusqueda.user.id,
          idDistribuidor
        )
        auth = new UserPassOrgAuthToken(userDetails, auth.credentials, userDetails.authorities, respuestaBusqueda.distribuidor)
        return auth
      }else{
        if( respuestaBusqueda.code == 1 ){
          throw new BadCredentialsException("Usuario not found")
        }else if( respuestaBusqueda.code == 2 ){
          throw new BadCredentialsException("Usuario not role found")
        }else if( respuestaBusqueda.code == 3 ){
          throw new BadCredentialsException("Usuario loging block")
        }
      }

   	}

}
