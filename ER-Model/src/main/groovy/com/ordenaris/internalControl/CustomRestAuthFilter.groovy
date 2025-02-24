// package com.ordenaris.internalControl

// import javax.servlet.FilterChain
// import javax.servlet.ServletException
// import javax.servlet.ServletRequest
// import javax.servlet.ServletResponse
// import org.springframework.security.core.context.SecurityContextHolder
// import javax.servlet.http.HttpServletRequest
// import javax.servlet.http.HttpServletResponse
// import org.springframework.security.core.Authentication
// import org.springframework.security.core.AuthenticationException
// import grails.plugin.springsecurity.rest.token.AccessToken
// import grails.plugin.springsecurity.rest.token.generation.TokenGenerator
// import grails.plugin.springsecurity.rest.token.storage.TokenStorageService
// import org.springframework.security.core.userdetails.UserDetails
// import grails.plugin.springsecurity.rest.RestAuthenticationFilter
// import com.google.common.io.CharStreams
// import groovy.json.JsonSlurper
// import groovy.json.JsonBuilder
// import grails.plugin.springsecurity.SpringSecurityUtils
// import org.springframework.security.authentication.BadCredentialsException
// import org.springframework.security.authentication.AuthenticationDetailsSource
// import grails.util.Holders
// import com.ordenaris.internalControl.UserService
// import com.ordenaris.internalControl.RegistroService
// import com.ordenaris.internalControl.FuncionService
// import com.ordenaris.api.Ordenaris
// import com.ordenaris.internalControl.Users
// import com.ordenaris.internalControl.Constants

// class CustomRestAuthFilter extends RestAuthenticationFilter {

// 	AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource
	
// 	CustomRestAuthenticationFailureHandler authenticationFailureHandler = new CustomRestAuthenticationFailureHandler()
// 	CustomRestAuthenticationSuccessHandler authenticationSuccessHandler = new CustomRestAuthenticationSuccessHandler()

// 	def ordenaris = new Ordenaris()

// 	private UserPassOrgAuthToken extractCredentialsFromJsonPayload(HttpServletRequest httpServletRequest){
// 		String username = httpServletRequest.JSON.username
// 		String crd = httpServletRequest.JSON.password
		
// 		if( username && crd ){
// 			return new UserPassOrgAuthToken(username, crd)
// 		}else{
// 			return null
// 		}
// 		username = ""
// 		crd = ""
// 	}

// 	@Override
// 	void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException{
// 		String endpointUrl =  "/api/login"

// 		HttpServletRequest httpServletRequest = request as HttpServletRequest
// 		HttpServletResponse httpServletResponse = response as HttpServletResponse

// 		def actualUri =  httpServletRequest.requestURI - httpServletRequest.contextPath


// 		def userService = Holders.grailsApplication.mainContext.getBean('userService')
// 		def funcionService = Holders.grailsApplication.mainContext.getBean('funcionService')
// 		def registroService = Holders.grailsApplication.mainContext.getBean('registroService')

// 		def logID
// 		if (actualUri == endpointUrl || actualUri == secondAuth || actualUri ==  thirdAuth ){

// 			if( request.getHeader("ordServicio") == "52cb7a88f0894bc79f7e7549676c6c5a" ){

// 				if (httpServletRequest.method != 'POST'){
// 					println "${httpServletRequest.method} HTTP method is not supported. Setting status to ${HttpServletResponse.SC_METHOD_NOT_ALLOWED}"
// 					httpServletResponse.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED)
// 					return
// 				}
// 				if( actualUri == endpointUrl ){
//     				logID = new Logs("Envío código inicio sesión.", "Inicio de solicitud.", request.getRequest(), request.getRequest().getHeader(Constants.HEADER_LOG_ID) ).getId()
//        				funcionService.logger( logID, "Envío código inicio sesión.", "Inicio de solicitud." )
//        				def enviaCorreo = userService.enviarCodigo( httpServletRequest.JSON.username.toLowerCase(), logID )
//        				httpServletResponse.contentType = 'application/json'
//        				httpServletResponse.characterEncoding = 'UTF-8'
//        				httpServletResponse << new JsonBuilder( enviaCorreo ).toPrettyString()
//        				return
// 				}else if( actualUri == secondAuth ){
// 					def correo = httpServletRequest.JSON.username.toLowerCase()
// 					logID = new Logs("Validar código inicio de sesión.", "Inicio de solicitud.", request.getRequest(), request.getRequest().getHeader(Constants.HEADER_LOG_ID) ).getId()
// 					funcionService.logger( logID, "Validar código inicio de sesión.", "Inicio de solicitud." )
// 					// se valida el codigo
// 					def validacion  = registroService.validarOTP( httpServletRequest.JSON.codigo, correo, logID ) 

//    					httpServletResponse.contentType = 'application/json'
//        				httpServletResponse.characterEncoding = 'UTF-8'
//        				httpServletResponse << new JsonBuilder( validacion ).toPrettyString()
//        				return
// 				}else if( actualUri == thirdAuth ){
// 					// buscando el correo y validando que el token sea correcto
// 					def validar  = userService.validarToken(  httpServletRequest.JSON  )
// 					if( !validar.success ){
// 						httpServletResponse.contentType = 'application/json'
// 	       				httpServletResponse.characterEncoding = 'UTF-8'
// 	       				httpServletResponse << new JsonBuilder( validar ).toPrettyString()
// 	       				return
// 					}

// 					httpServletRequest.JSON.username = validar.username 

// 					CustomAuthProvider customAuthProvider = new CustomAuthProvider()


// 					Authentication authentication = SecurityContextHolder.getContext().getAuthentication()
// 					Authentication authenticationResult

// 					UserPassOrgAuthToken authenticationRequest = this.extractCredentialsFromJsonPayload(httpServletRequest)

// 					if( authenticationRequest ){
						
// 						boolean authenticationRequestIsCorrect = (
// 							authenticationRequest?.principal && 
// 							authenticationRequest?.credentials
// 						)
						
// 						if(authenticationRequestIsCorrect){
// 				           	try{
// 				         		authenticationResult = customAuthProvider.authenticate(authenticationRequest)
				         		
// 				           		if (authenticationResult.authenticated){
// 				           			SecurityContextHolder.context.setAuthentication(authenticationResult)
// 				           		}
// 				           	}catch (AuthenticationException ae){
// 				           		authenticationFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, ae)
// 				           		return
// 				           	}
// 				        }else{
// 				          	if(!authentication){
// 				           		httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST)
// 				           		return
// 				           	}else{
// 				           		authenticationResult = authentication
// 				           	}
// 				        }
// 				        if (authenticationResult?.authenticated){
// 				           	// def userService = Holders.grailsApplication.mainContext.getBean('userService')
				           	
// 				           	def accessToken = userService.getToken( authenticationResult.principal as UserDetails )
// 				           	authenticationSuccessHandler.onAuthenticationSuccess(httpServletRequest, httpServletResponse, accessToken)
// 				        }else{
// 				        	httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED)
// 				        	return
// 				        }
// 					}else{
// 						httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST)
// 						return
// 					}

// 				}
				
// 			}else{
// 				httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED)
// 				return
// 			}

//         }else{
//         	chain.doFilter(request, response)
//         }
//     }
// }