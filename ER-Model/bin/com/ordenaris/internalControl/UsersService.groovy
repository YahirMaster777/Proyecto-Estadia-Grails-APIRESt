package com.ordenaris.internalControl

import grails.gorm.transactions.Transactional
import grails.gorm.CriteriaBuilder
import org.springframework.security.core.authority.AuthorityUtils
import grails.plugin.springsecurity.rest.token.AccessToken
import grails.plugin.springsecurity.rest.token.storage.TokenStorageService

@Transactional
class UsersService {
def tokenGenerator, tokenStorageService
def springSecurityService
def authenticationEventPublisher

    def createUser(data, logId) {
        Users.withTransaction{uStatus ->
            try{
                new Logs("Registrar usuario", "Procesando Solicitud", logId, "INFO", true, [data:data.username])
                Utils.logger(logId,"Registrar usuario","Procesando Solicitud", "Nombre de usuario:${data.username}")
                def employee = Employees.findByUuid(data.employeeUuid)
                if (!employee) {
                    new Logs( "Registrar usuario", "No se encontró el registro", logId, "ERROR", false, [ uuidEmployee:data.employeeUuid ] )
                    Utils.logger(logId, "Registrar usuario", "No se encontró el registro", "Empleado:${data.employeeUuid}")
                    return TypeError.informationNotFound( logId )
                }
                def user = new Users()
                user.username = data.username
                user.password = data.password
                user.employee = employee
                data.businessEmail?user.businessEmail=data.businessEmail:user.businessEmail
                user.save(flush: true, failOnError:true)
                new Logs("Registrar usuario", "Se registro el usuario", logId,"INFO", true,[data:data.username])
                Utils.logger(logId, "Registrar usuario", "Se registro el usuario", "Nombre de usuario:${data.username}")
                return [ data: [ success: true], status: 200 ]
            }catch(e){
                uStatus.setRollbackOnly()
                new Logs("Registrar usuario","Error en la solicitud al crear un usuario", logId, e, [ : ])
                Utils.logger(logId, "Registrar usuario", "Error en la solicitud al crear un usuario", "f: ${e.getMessage()}")
                return TypeError.internalError(logId)
            }
        }
    }

    def updateUser(data, uuid, logId){
        Users.withTransaction{uStatus ->
            try {
                new Logs("Actualizar usuario", "Procesando Solicitud", logId, "INFO", true, [uuidUser:uuid])
                Utils.logger(logId,"Actualizar usuario","Procesando Solicitud", uuid)
                def employee = Employees.findByUuid(data.employeeUuid)
                if (!employee) {
                    new Logs( "Actualizar usuario", "No se encontró el registro", logId, "ERROR", false, [ uuidEmployee:data.employeeUuid ] )
                    Utils.logger(logId, "Actualizar usuario", "No se encontró el registro", "Empleado:${data.employeeUuid}")
                    return TypeError.informationNotFound( logId )
                }
                def user = Users.findByUuid(uuid)
                if (!user) {
                    new Logs( "Actualizar usuario", "No se encontró el registro", logId, "ERROR", false, [ uuidUser:uuid ] )
                    Utils.logger(logId, "Actualizar usuario", "No se encontró el registro", "Usuario:${uuid}")
                    return TypeError.informationNotFound( logId )
                }
                user.username = data.username
                user.businessEmail = data.businessEmail
                user.employee = employee
                user.save(flush:true, failOnError:true)
                new Logs("Actualizar usuario", "Se actualizo el usuario", logId,"INFO", true,[data:data.username])
                Utils.logger(logId, "Actualizar usuario", "Se actualizo el usuario", "Nombre de usuario:${data.username}")
                return [ data: [ success: true], status: 200 ]
            } catch(Exception e) {
                uStatus.setRollbackOnly()
                new Logs("Actualizar usuario","Error en la solicitud al actualizar un usuario", logId, e, [ : ])
                Utils.logger(logId, "Actualizar usuario", "Error en la solicitud al actualizar un usuario", "f: ${e.getMessage()}")
                return TypeError.internalError( logId )
            }
        }
    }   

    @Transactional(readOnly = true)
    def buscarCuenta(UserPassOrgAuthToken auth){
        def username = auth.name
        Users user = Users.findByUsername(username)
        return user
    }
    
    def getUserAuthorities( Users username ){
        def userRoles = UsersRoles.findAllByUser(username)
        def authorities = []
        if(userRoles.size() > 0){
            authorities = userRoles.role.authority
        }        
        return AuthorityUtils.createAuthorityList(authorities as String[])
    }

    @Transactional(readOnly = true)
    def infoUsers( Users username ){
        try {
            def user = Users.findByUsername(username.username)
            def userSectionPermission = UserSectionPermission.findAllByUser(username)
            def seccionesAgrupadas = [:]
            UserSectionPermission.findAllByUser(username).each{templatePermission ->
                def permiso = templatePermission.permission 
                def seccion = permiso?.section  
                if (seccion && permiso) {
                    if (!seccionesAgrupadas.containsKey(seccion.name)) {
                        seccionesAgrupadas[seccion.name] = [:]
                    }
                    seccionesAgrupadas[seccion.name][permiso.name] = permiso.alias
                }
            }
            def section = seccionesAgrupadas.collect { nombreSeccion, permisos ->
                return [
                    seccion  : nombreSeccion,
                    permisos : permisos
                ]
            }
            def uuidEmployee = user?.employee.uuid
            def employee = Employees.findByUuid(uuidEmployee)
            def response =[
                uuid          : user.uuid,
                employee      : "${employee.name} ${employee.lastName1} ${employee.lastName2}",
                secctions     : section,
            ]
            return  response
        }catch(Exception e) {
            println e.getMessage()
        }   
    }
    
    def getToken( userDetails ){
        AccessToken accessToken = tokenGenerator.generateAccessToken(userDetails)
        tokenStorageService.storeToken(accessToken.accessToken, userDetails)
        authenticationEventPublisher.publishAuthenticationSuccess( springSecurityService.getAuthentication() )
        return accessToken
    }

    @Transactional(readOnly = true)
    def readUser(uuid, logId) {
        try {
            new Logs("Buscar usuario", "Procesando Solicitud", logId, "INFO", true, [uuidUser:uuid])
            Utils.logger(logId,"Buscar usuario","Procesando Solicitud", uuid)
            def user = Users.findByUuid(uuid)
            if (!user) {
                new Logs( "Buscar usuario", "No se encontró el registro", logId, "ERROR", false, [ uuidUser:uuid ] )
                Utils.logger(logId, "Buscar usuario", "No se encontró el registro", "Usuario:${uuid}")
                return TypeError.informationNotFound( logId )
            }
            new Logs( "Buscar usuario", "Usuario encontrado", logId, "INFO", true, [ data: uuid ] )
            Utils.logger(logId, "Buscar usuario", "Usuario encontrado", uuid)
            return [ data: [success: true, data:constructorUser(user) ], status: 200 ]
        }catch(Exception e) {
            new Logs("Buscar usuario","Error en la solicitud al buscar el usuario", logId, e, [ : ])
            Utils.logger(logId, "Buscar usuario", "Error en la solicitud al buscar el usuario", "f: ${e.getMessage()}")
            return TypeError.internalError( logId )
        }
    } 

    def deleteUser(uuid, logId) {
        Users.withTransaction{ uStatus->
            try {
                new Logs("Eliminar usuario", "Procesando Solicitud", logId, "INFO", true, [uuidUser:uuid])
                Utils.logger(logId,"Eliminar usuario","Procesando Solicitud", uuid)
                def user = Users.findByUuid(uuid)
                if (!user) {
                    new Logs( "Eliminar usuario", "No se encontró el registro", logId, "ERROR", false, [ uuidUser:uuid ] )
                    Utils.logger(logId, "Eliminar usuario", "No se encontró el registro", "Usuario:${uuid}")
                    return TypeError.informationNotFound( logId )
                }
                user.delete(flush:true, failOnError:true)
                new Logs("Eliminar usuario", "Se elimino el usuario", logId,"INFO", true,[uuidUser:uuid])
                Utils.logger(logId, "Eliminar usuario", "Se elimino el usuario", uuid)
                return [ data: [ success: true], status: 200 ]
            } catch(Exception e) {
                uStatus.setRollbackOnly()
                new Logs("Eliminar usuario","Error en la solicitud al eliminar el usuario", logId, e, [ : ])
                Utils.logger(logId, "Eliminar usuario", "Error en la solicitud al eliminar el usuario", "f: ${e.getMessage()}")
                return TypeError.internalError( logId )
            }
        }
    }

    @Transactional(readOnly = true)
    def listUser(params, logId) {
        try{
            new Logs("Páginado usuario", "Procesando Solicitud", logId, "INFO", true, [ : ])
            Utils.logger(logId,"Páginado usuario","Procesando Solicitud")
            int page = (params.int('page') ?:1) -1
            int max = params.int('max') ?:10
            int offset = page * max
            params.sort?params.sort:"id"
            params.order?params.order:"asc"
            def users = Users.createCriteria().list(max:max, offset:offset) {
                if(params.search) {
                    sqlRestriction("lower(concat(business_email, ' ' ,username)) like '%${params.search.toLowerCase().replaceAll(" ","%")}%'")
                }
                order(params.sort, params.order.toLowerCase())
            }.collect{ constructorUser(it)}
            def userCount = Users.withCriteria {
                if(params.search) {
                    sqlRestriction("lower(concat(business_email, ' ' ,username)) like '%${params.search.toLowerCase().replaceAll(" ","%")}%'")
                }
                projections {
                    rowCount()
                }
            }[0]
            new Logs("Páginado usuario", "Resultados de la busqueda usuario", logId,"INFO", true,[ : ])
            Utils.logger(logId, "Páginado usuario", "Resultados de la busqueda usuario")
            return [ data: [ success: true, data: [list: users, total: userCount]], status: 200 ]
        } catch(Exception e) {
            new Logs("Páginado usuario","Error en la solicitud al obtener el paginado", logId, e, [ : ])
            Utils.logger(logId, "Páginado usuario", "Error en la solicitud al obtener el paginado", "f: ${e.getMessage()}")
            return TypeError.internalError( logId )
        }
    }

    @Transactional(readOnly = true)
    def allUser(logId) {
        try {
            new Logs( "Páginado usuario", "Procesando Solicitud", logId, "INFO", true, [ : ] )
            Utils.logger(logId, "Páginado usuario","Procesando Solicitud" )
            def user = Users.createCriteria().list() {}
            def userList = user.collect { constructorUser(it)}
            def userCount = Users.count()
            new Logs( "Páginado usuario", "Usuarios encontrados", logId, "INFO", true, [ : ] )
            Utils.logger(logId, "Páginado usuario","Usuarios encontrados" )
            return [data: [success: true, data: userList, total: userCount], status: 200]
        } catch(Exception e) {
            new Logs( "Páginado usuario", "Error en la solicitud al mostrar los resultados", logId, e, [ : ] )
            Utils.logger(logId, "Páginado usuario", "Error en la solicitud al mostrar los resultados", "f: ${e.getMessage()}")
            return TypeError.internalError( logId )
        }
    }

    // @Transactional(readOnly = true)
    // def infoUser(username,logid) {
    //     try {
    //         new Logs("Información del usuario", "Procesando Solicitud", logId, "INFO", true, [uuidUser:uuid])
    //         Utils.logger(logId,"Información del usuario","Procesando Solicitud", uuid)
    //         def user = Users.findByUsername(username)
    //         if (!user) {
    //             new Logs( "Información del usuario", "No se encontró el registro", logId, "ERROR", false, [ uuidUser:uuid ] )
    //             Utils.logger(logId, "Información del usuario", "No se encontró el registro", "Usuario:${uuid}")
    //             return TypeError.informationNotFound( logId )
    //         }
    //         def userInfo = Users.createCriteria().list(){
    //             sqlRestriction()

    //         }.collect{ constructorTemplatePermission(it) }
    //         println user
    //         new Logs( "Información del usuario", "Se muestra la inforrmación al inciar sesión", logId, "INFO", true, [ data: uuid ] )
    //         Utils.logger(logId, "Información del usuario", "Se muestra la inforrmación al inciar sesión", uuid)
    //         return [ data: [success: true, data:userInfo ], status: 200 ]
    //     } catch(Exception e) {
    //         new Logs("Información del usuario","Error en la solicitud de información", logId, e, [ : ])
    //         Utils.logger(logId, "Información del usuario", "Error en la solicitud de información", "f: ${e.getMessage()}")
    //         return TypeError.internalError( logId )
    //     }
    // }

    def constructorTemplatePermission(templatePermission) {
        def templates = templatePermission.template.collect{
            constructorTemplate(it)
        }
        return [
            seccion : templatePermission.seccion,
            template : templates
        ]
    }

    def constructorSeccion(section) {
        return [
            seccion: section.name,
            permission: section.permission
        ]
    }

    def constructorUser(user) {
        return [
            uuid          : user.uuid,
            username      : user.username,
            businessEmail : user.businessEmail
        ]
    }
}