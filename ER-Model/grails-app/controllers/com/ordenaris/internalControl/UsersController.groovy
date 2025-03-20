package com.ordenaris.internalControl

import grails.rest.*
import grails.converters.*

class UsersController {
	static responseFormats = ['json', 'xml']
    def UsersService
    
    def create() { 
        def data = request.JSON
        def logId = new Logs("Registrar usuario","Inicio de solicitud", request, request.getHeader(Constants.HEADER_LOG_ID)).getId()
        Utils.logger(logId, "Registrar usuario","Inicio de solicitud")
        if(!Utils.validateAccessProject( request.getHeader(Constants.HEADER_ORD_SERVICE))) return respond(TypeError.noPermissions(logId))
        def validDataExist = [
            ['nombre de usuario':data.username],
            ['contraseña':data.password],
            ['emplado uuid':data.employeeUuid]
        ]
        def isDataExist = Utils.dataRequired(validDataExist, "Registrar usuario" , logId)
        if (isDataExist.status != 200) return respond(isDataExist.data, status:isDataExist.status)
        def isValidData = validFormatData("Registrar usuario", data, logId)
        if (isValidData.status != 200) return respond(isValidData.data, status: isValidData.status)
        def responseService= UsersService.createUser(data, logId)
        return respond(responseService.data, status:responseService.status)
    }

    def  update() {
        def data = request.JSON
        def logId = new Logs("Actualizar usuario", "Inicio de solicitud", request, request.getHeader(Constants.HEADER_LOG_ID)).getId()
        Utils.logger(logId, "Actualizar usuario", "Inicio de solicitud", params.uuid)
        if(!Utils.validateAccessProject( request.getHeader(Constants.HEADER_ORD_SERVICE))) return respond(TypeError.noPermissions(logId))
        def isValidData = validFormatData("Actualizar usuario",data, logId)
        if (isValidData.status != 200) return respond(isValidData.data, status: isValidData.status )
        def responseService = UsersService.updateUser(data, params.uuid, logId)
        return respond(responseService.data, status: responseService.status )
    }

    def read() {
        def logId = new Logs("Buscar usuario", "Inicio de solicitud", request, request.getHeader(Constants.HEADER_LOG_ID)).getId()
        Utils.logger(logId, "Buscar usuario", "Inicio de solicitud", params.uuid)
        if(!Utils.validateAccessProject( request.getHeader(Constants.HEADER_ORD_SERVICE))) return respond(TypeError.noPermissions(logId))
        def responseService = UsersService.readUser(params.uuid, logId)
        return respond(responseService.data, status: responseService.status)
    }

    def delete() {
        def logId = new Logs("Eliminar usuario", "Inicio de solicitud", request, request.getHeader(Constants.HEADER_LOG_ID)).getId()
        Utils.logger(logId, "Eliminar usuario", "Inicio de solicitud", params.uuid)
        if(!Utils.validateAccessProject( request.getHeader(Constants.HEADER_ORD_SERVICE))) return respond(TypeError.noPermissions(logId))
        def responseService = UsersService.deleteUser(params.uuid, logId)
        return respond(responseService.data, status: responseService.status)
    }

    def list() {
        def logId = new Logs("Páginado usuario", "Inicio de solicitud", request, request.getHeader(Constants.HEADER_LOG_ID)).getId()
        Utils.logger(logId, "Páginado usuario","Inicio de solicitud")
        if(!Utils.validateAccessProject( request.getHeader(Constants.HEADER_ORD_SERVICE))) return respond(TypeError.noPermissions(logId))
        def isValidParams = Utils.validPaginationFormat(params, "usuario", ["username", "businessEmail"] , logId)
        if (isValidParams.status != 200) return respond(isValidParams.data, status: isValidParams.status )
        def responseService = UsersService.listUser(params, logId)
        return respond(responseService.data, status: responseService.status)
    }
    
    def all() {
        def logId = new Logs("Lista de usuarios", "Inicio de solicitud", request, request.getHeader(Constants.HEADER_LOG_ID)).getId()
        Utils.logger(logId,"Lista de usuarios", "Inicio de solicitud")
        if(!Utils.validateAccessProject( request.getHeader(Constants.HEADER_ORD_SERVICE))) return respond(TypeError.noPermissions(logId))
        def responseService = UsersService.allUser( logId)
        return respond(responseService.data, status: responseService.status)
    }

    def validFormatData(process,data, logId) {
        if (data.username  && !data.username.isInstitutionalEmail()) {
            new Logs( process, "El nombre de usuario no coincide con el formato esperado", logId, "ERROR", false, [  data: data.username ] )
            Utils.logger(logId, process, "El nombre de usuario no coincide con el formato esperado", data.username)
            return TypeError.incorrectFormat( "nombre de usuario", "correo empresarial", logId )
        }
        if(data.password && !data.password.isPassword()) {
            new Logs( "Registrar usuario", "La contraseña no coincide con el formato esperado ", logId, "ERROR", false, [  data: data.password ] )
            Utils.logger(logId, "Registrar usuario", "La contraseña no coincide con el formato esperado", data.password)
            def validPasswordResponse = TypeError.incorrectFormat( "contraseña", "minimo 8 de caracteres, al menos una letra mayúscula, una letra minucula, un número, sin espacios y un caracter especial", logId )
            return respond(validPasswordResponse.data, status:validPasswordResponse.status)
        }
        if (data.businessEmail && (!data.businessEmail.isInstitutionalEmail())) {
            new Logs( process, "El correo electronico no coincide con el formato esperado", logId, "ERROR", false, [ data: data.businessEmail])
            Utils.logger(logId, process, "El correo electronico no coincide con el formato esperado", data.businessEmail)
            return TypeError.incorrectFormat("correo electronico", "correo electronico valido", logId)
        }
        // TODO: Cambiar esta parte porque no deberia tener conexión a la bd
        def userList = Users.createCriteria().list() {
            or{
                ilike('username', data.username?:'')
                ilike('businessEmail', data.businessEmail?:'')
            }   
        }.collect()
        if (!userList.isEmpty()) {
            new Logs(process, "Datos existentes, por favor utilice valores diferentes", logId, "INFO", false, [data: [success: false]])
            Utils.logger(logId, process, "Datos existentes, por favor utilice valores diferentes" )
            return TypeError.existingRegister(logId)
        }
        if (data.employeeUuid) {
            def isValidUuid = Utils.validFormatUuid(process, "emplado uuid",data.employeeUuid, logId)
            if(isValidUuid.status != 200) return isValidUuid
        }
        new Logs( process, "Validar datos de usuario", logId, "INFO", true, [ : ] )
        Utils.logger(logId, process, "Validar datos de usuario")
        return [data: [success: true], status:200]
    }
}