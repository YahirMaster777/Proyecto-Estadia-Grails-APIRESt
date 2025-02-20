package com.ordenaris.internalControl

import grails.gorm.transactions.Transactional

@Transactional
class UsersService {

    def createUser(data, logId) {
        Users.withTransaction{uStatus ->
            try{
                new Logs("Registrar usuario", "Procesando Solicitud", logId, "INFO", true, [data:data.username])
                Utils.logger(logId,"Registrar usuario","Procesando Solicitud", "Nombre de usuario:${data.username}")
                def user = new Users()
                user.username = data.username
                user.password = data.password
                user.save(flush: true, failOnError:true)
                new Logs("Registrar usuario", "Se registro el usuario", logId,"INFO", true,[data:data.username])
                Utils.logger(logId, "Registrar usuario", "Se registro el usuario", "Nombre de usuario:${data.username}")
                return [ data: [ success: true], status: 200 ]
            }catch(e){
                uStatus.setRollbackOnly()
                new Logs("Registrar usuario","Error en la solicitud", logId, e, [ : ])
                Utils.logger(logId, "Registrar usuario", "Error en la solicitud", "f: ${e.getMessage()}")
                return TypeError.internalError(logId)
            }
        }
    }

    def updateUser(data, uuid, logId){
        Users.withTransaction{uStatus ->
            try {
                new Logs("Actualizar usuario", "Procesando Solicitud", logId, "INFO", true, [uuidUser:uuid])
                Utils.logger(logId,"Actualizar usuario","Procesando Solicitud", uuid)
                println 
                println data.employeeUuid
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
                new Logs("Registrar usuario", "Se registro el usuario", logId,"INFO", true,[data:data.username])
                Utils.logger(logId, "Registrar usuario", "Se registro el usuario", "Nombre de usuario:${data.username}")
                return [ data: [ success: true], status: 200 ]
            } catch(Exception e) {
                uStatus.setRollbackOnly()
                new Logs("Actualizar usuario","Error en la solicitud", logId, e, [ : ])
                Utils.logger(logId, "Actualizar usuario", "Error en la solicitud", "f: ${e.getMessage()}")
                return TypeError.internalError(logId)
            }
        }
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
            new Logs("Buscar usuario","Error en la solicitud", logId, e, [ : ])
            Utils.logger(logId, "Buscar usuario", "Error en la solicitud", "f: ${e.getMessage()}")
            return TypeError.internalError(logId)
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
                user.accountExpired = 1
                user.save(flush:true, failOnError:true)
                new Logs("Eliminar usuario", "Se elimino el usuario", logId,"INFO", true,[uuidUser:uuid])
                Utils.logger(logId, "Eliminar usuario", "Se elimino el usuario", "Nombre:${data.uuid}")
            } catch(Exception e) {
                uStatus.setRollbackOnly()
                new Logs("Eliminar usuario","Error en la solicitud", logId, e, [ : ])
                Utils.logger(logId, "Eliminar usuario", "Error en la solicitud", "f: ${e.getMessage()}")
            }
        }
    }

    @Transactional(readOnly = true)
    def listUser(params, logId) {

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
            new Logs( "Páginado usuario", "Ha ocurrido una excepción al obtener los usuarios", logId, e, [ : ] )
            Utils.logger(logId, "Páginado usuario", "Atrapando errorres", "f: ${e.getMessage()}")
            return TypeError.internalError( logId )
        }
    }

    // TODO: metodo para recuperar la contraseña
    def constructorUser(user) {
        return [
            uuid: user.uuid,
            username: user.username,
            businessEmail: user.businessEmail
        ]
    }
}
