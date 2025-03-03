package com.ordenaris.internalControl

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import org.springframework.security.core.context.SecurityContextHolder
<<<<<<< HEAD
=======

>>>>>>> 32e19a9 (implementacion de login dinamico)
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import grails.plugin.springsecurity.rest.token.AccessToken
import grails.plugin.springsecurity.rest.token.generation.TokenGenerator
import grails.plugin.springsecurity.rest.token.storage.TokenStorageService
import org.springframework.security.core.userdetails.UserDetails
import grails.plugin.springsecurity.rest.RestAuthenticationFilter
import com.google.common.io.CharStreams
import groovy.json.JsonSlurper
import groovy.json.JsonBuilder
import grails.plugin.springsecurity.SpringSecurityUtils
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.AuthenticationDetailsSource
import grails.util.Holders
import com.ordenaris.distribuidores.UserService
import com.ordenaris.distribuidores.RegistroService
import com.ordenaris.distribuidores.FuncionService
import com.ordenaris.api.Ordenaris
import com.ordenaris.distribuidores.User
import com.ordenaris.distribuidores.Constants

class CustomRestAuthFilter extends RestAuthenticationFilter {

	AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource
	
	CustomRestAuthenticationFailureHandler authenticationFailureHandler = new CustomRestAuthenticationFailureHandler()
	CustomRestAuthenticationSuccessHandler authenticationSuccessHandler = new CustomRestAuthenticationSuccessHandler()

	def ordenaris = new Ordenaris()

	private UserPassOrgAuthToken extractCredentialsFromJsonPayload(HttpServletRequest httpServletRequest){
		String username = httpServletRequest.JSON.username
		String crd = httpServletRequest.JSON.password
		
		if( username && crd ){
			return new UserPassOrgAuthToken(username, crd)
		}else{
			return null
		}
		username = ""
		crd = ""
	}

	@Override
	void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException{
		String endpointUrl =  "/api/login"
		// authenticationFailureHandler.setStatusCode( HttpServletResponse.SC_UNAUTHORIZED )

		HttpServletRequest httpServletRequest = request as HttpServletRequest
		HttpServletResponse httpServletResponse = response as HttpServletResponse

		def actualUri =  httpServletRequest.requestURI - httpServletRequest.contextPath
		if (actualUri == endpointUrl){


			if (httpServletRequest.method != 'POST'){
				println "${httpServletRequest.method} HTTP method is not supported. Setting status to ${HttpServletResponse.SC_METHOD_NOT_ALLOWED}"
				httpServletResponse.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED)
				return
			}
			CustomAuthProvider customAuthProvider = new CustomAuthProvider()


			Authentication authentication = SecurityContextHolder.getContext().getAuthentication()
			Authentication authenticationResult

			UserPassOrgAuthToken authenticationRequest = this.extractCredentialsFromJsonPayload(httpServletRequest)

			if( authenticationRequest ){
				
				boolean authenticationRequestIsCorrect = (
					authenticationRequest?.principal && 
					authenticationRequest?.credentials
				)
				
				if(authenticationRequestIsCorrect){
		           	try{
		         		authenticationResult = customAuthProvider.authenticate(authenticationRequest)
		         		
		           		if (authenticationResult.authenticated){
		           			SecurityContextHolder.context.setAuthentication(authenticationResult)
		           		}
		           	}catch (AuthenticationException ae){
		           		authenticationFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, ae)
		           		return
		           	}
		        }else{
		          	if(!authentication){
		           		httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST)
		           		return
		           	}else{
		           		authenticationResult = authentication
		           	}
		        }
		        if (authenticationResult?.authenticated){
		           	def userService = Holders.grailsApplication.mainContext.getBean('usersService')
		           	def accessToken = userService.getToken( authenticationResult.principal as UserDetails )
		           	authenticationSuccessHandler.onAuthenticationSuccess(httpServletRequest, httpServletResponse, accessToken)
		        }else{
		        	httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED)
		        	return
		        }
			}else{
				httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST)
				return
			}

        }else{
        	chain.doFilter(request, response)
        }
    }
}