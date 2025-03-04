package com.ordenaris.internalControl

import com.ordenaris.internalControl.*

import java.util.Collection;

import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
// import org.springframework.aop.aspectj.RuntimeTestWalker.ThisInstanceOfResidueTestVisitor
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
	def userService = Holders.grailsApplication.mainContext.getBean('usersService')

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

      def respuestaBusqueda = userService.buscarCuenta(auth)
      println 'usuario: '+respuestaBusqueda
      def getUserAuthorities = userService.getUserAuthorities(respuestaBusqueda)
      println 'roles:'+getUserAuthorities
     
      if( respuestaBusqueda ){
        // fnVerifyStatusUser( respuestaBusqueda )


        def userDetails = new MyUserDetails(
          respuestaBusqueda.username,
          respuestaBusqueda.password,
          respuestaBusqueda.enabled,
          !respuestaBusqueda.accountExpired,
          !respuestaBusqueda.passwordExpired,
          !respuestaBusqueda.accountLocked,
          getUserAuthorities,
          respuestaBusqueda.id
         
        )
        auth = new UserPassOrgAuthToken(userDetails, auth.credentials, userDetails.authorities)
        println auth
        return auth
      }else{
          println 'Error'
      }

   	}

}
