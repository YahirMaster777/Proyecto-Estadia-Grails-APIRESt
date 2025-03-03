grails.gorm.failOnError = true // Si ocurre un error al guardar datos en la base, lanza una excepción.
grails.gorm.autoFlush = true  // Habilita el auto-flush para que las operaciones de base de datos se ejecuten inmediatamente.

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.ordenaris.internalControl.Users'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.ordenaris.internalControl.UsersRoles'
grails.plugin.springsecurity.authority.className = 'com.ordenaris.internalControl.Roles'
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	[pattern: '/',               access: ['permitAll']],
	[pattern: '/error',          access: ['permitAll']],
	[pattern: '/index',          access: ['permitAll']],
	[pattern: '/index.gsp',      access: ['permitAll']],
	[pattern: '/shutdown',       access: ['permitAll']],
	[pattern: '/assets/**',      access: ['permitAll']],
	[pattern: '/**/js/**',       access: ['permitAll']],
	[pattern: '/**/css/**',      access: ['permitAll']],
	[pattern: '/**/images/**',   access: ['permitAll']],
	[pattern: '/**/favicon.ico', access: ['permitAll']],
	[pattern: '/recovery/**',    access: ['permitAll']],
    [pattern: '/apps/**',        access: ['ROLE_ROOT','ROLE_ADMIN']],
    [pattern: '/profiles/**',    access: ['ROLE_ROOT','ROLE_ADMIN']],
    [pattern: '/profilePermissions/**',    access: ['ROLE_ROOT','ROLE_ADMIN']],
    [pattern: '/appConnections/**',    access: ['ROLE_ROOT','ROLE_ADMIN']],
    [pattern: '/sections/**',    access: ['ROLE_ROOT','ROLE_ADMIN']],
	[pattern: '/servers/**',     access: ['ROLE_ROOT', 'ROLE_ADMIN', 'ROLE_CUSTOM']],
	[pattern: '/users/**',       access: ['ROLE_ROOT', 'ROLE_ADMIN', 'ROLE_CUSTOM']],
	[pattern: '/employees/**',   access: ['ROLE_ROOT', 'ROLE_ADMIN', 'ROLE_CUSTOM']],
	[pattern: '/serversApps/**', access: ['ROLE_ROOT', 'ROLE_ADMIN', 'ROLE_CUSTOM']],

]

    grails.plugin.springsecurity.filterChain.chainMap = [
	[pattern:"/api/**", filters:"JOINED_FILTERS,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter,-authenticationFilter"],
	[pattern:"/auth/**", filters:"JOINED_FILTERS,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter,-rememberMeAuthenticationFilter"],
	[pattern:"/admin/**", filters:"JOINED_FILTERS,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter,-authenticationFilter"],
	[pattern:"/public/**", filters:"anonymousAuthenticationFilter,restTokenValidationFilter,restExceptionTranslationFilter,filterInvocationInterceptor"]	
    [pattern:"/admin/$uuid/**", filters:"JOINED_FILTERS,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter,-authenticationFilter"],
]

// Configuración de validación de tokens en el plugin Spring Security REST
grails.plugin.springsecurity.rest.token.validation.enableAnonymousAccess = true
grails.plugin.springsecurity.rest.token.validation.useBearerToken = true // Usar Bearer para los tokens para poder realizar las peticiones.
grails.plugin.springsecurity.rest.token.validation.headerName = 'X-Auth-Token'
grails.plugin.springsecurity.rest.token.storage.jwt.expiration = 86400  // Duración del token
// Endpoint para validar tokens
grails.plugin.springsecurity.rest.token.validation.active=true // Habilita la validación de tokens.
grails.plugin.springsecurity.rest.token.validation.endpointUrl='/api/validate' // Define el endpoint para validar tokens.
// Configuración de inicio de sesión en el plugin Spring Security REST
grails.plugin.springsecurity.rest.login.active=true
grails.plugin.springsecurity.rest.login.useJsonCredentials = true
grails.plugin.springsecurity.rest.login.failureStatusCode = 401
grails.plugin.springsecurity.rest.login.usernamePropertyName = 'username' // Parametro para el inicio de sesión.
grails.plugin.springsecurity.rest.login.passwordPropertyName='password' // Parametro para el inicio de sesión.
grails.plugin.springsecurity.rest.login.endpointUrl='/api/login' // ruta para el inicio de sesión ---> localhost:8080/api/login.
grails.plugin.springsecurity.rest.login.useRequestParamsCredentials = false
grails.plugin.springsecurity.useSecurityEventListener = true // Activar eventos

grails.plugin.springsecurity.onInteractiveAuthenticationSuccessEvent = { e, appCtx ->
    Users.withTransaction {
        def user = Users.findById(appCtx.springSecurityService.principal.id)
        if(!user.isAttached())
            user.attach()
        user.lastLoginTime = new Date() // actualizar la fecha de inicio de sesion
        user.save(flush: true, failOnError: true)
    }
}
grails.plugin.springsecurity.userDetailsService = 'myUserDetailsService'
grails.plugin.springsecurity.rest.token.rendering.jsonRenderer = 'com.ordenaris.internalControl.CustomAccessTokenJsonRenderer'
