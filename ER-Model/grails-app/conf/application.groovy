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
    [pattern: '/user/**', access: ['ROLE_ADMIN','ROLE_ROOT']],
]

grails.plugin.springsecurity.filterChain.chainMap = [
	[pattern:"/api/**", filters:"JOINED_FILTERS,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter,-authenticationFilter"],
    [pattern:"/admin/**", filters:"JOINED_FILTERS,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter,-authenticationFilter"],
    [pattern:"/auth/**", filters:"JOINED_FILTERS,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter,-rememberMeAuthenticationFilter"],
    [pattern:"/public/**", filters:"anonymousAuthenticationFilter,restTokenValidationFilter,restExceptionTranslationFilter,filterInvocationInterceptor"],	
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
