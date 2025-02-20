package com.ordenaris.internalControl

import grails.rest.*
import grails.converters.*

class UsersController {
	static responseFormats = ['json', 'xml']
    def UsersService
    def responseHeader = request.getHeader("x-request-id")
	
    def create() { 
        def data = request.JSON
        def logId = new Logs("Registrar usuario","Inicio de solicitud", request, responseHeader).getId()
        Utils.logger(logId,"Registrar usuario","Inicio de solicuitud")
        def isValidData = validFormatData("Registrar usuario", data, logId)
        if (isValidData.status != 200) return respond(isValidData.data, status: isValidData.status)
        def responseService= UsersService.createUser(data, logId)
        return respond(responseService.data, status:responseService.status)
    }

    def  update() {
        def data = request.JSON
        def logId = new Logs("Actualizar usuario", "Inicio de solicitud", request, responseHeader).getId()
        Utils.logger(logId, "Actualizar usuario", "Inicio de solicitud", params.uuid)
        def isValidData = validFormatData("Actualizar usuario",data, logId)
        if (isValidData.status != 200) return respond(isValidData.data, status: isValidData.status )
        def responseService = UsersService.updateUser(data, params.uuid, logId)
        return respond(responseService.data, status: responseService.status )
    }

    def read() {
        def logId = new Logs("Buscar usuario", "Inicio de solicitud", request, responseHeader).getId()
        Utils.logger(logId, "Buscar usuario", "Inicio de solicitud", params.uuid)
        def responseService = UsersService.readUser(params.uuid, logId)
        return respond(responseService.data, status: responseService.status)
    }

    def delete() {
        def logId = new Logs("Eliminar usuario", "Inicio de solicitud", request, responseHeader).getId()
        Utils.logger(logId, "Eliminar usuario", "Inicio de solicitud", params.uuid)
        def responseService = UsersService.deleteUser(params.uuid, logId)
        return respond(responseService.data, status: responseService.status)
    }

    def list() {
        def logId = new Logs("Páginado usuario", "Inicio de solicitud", request, responseHeader).getId()
        Utils.logger(logId, "Páginado usuario","Inicio de solicitud")
        def isValidParams = Utils.validFormatParams(params, "usuario", ["username", "uuid", "businessEmail"] , logId)
        if (isValidParams.status != 200) return respond(isValidParams.data, status: isValidParams.status )
        def responseService = UsersService.listUser(params, logId)
        return respond(responseService.data, status: responseService.status)
    }
    
    def all() {
        def logId = new Logs("Lista de usuarios", "Muestra todos los registros de la tabla usuarios", request, responseHeader).getId()
        Utils.logger(logId,"Lista de usuarios", "Muestra todos los registros de la tabla usuarios")
        def responseService = UsersService.allUser( logId)
        return respond(responseService.data, status: responseService.status)
    }

    def validFormatData(process,data, logId) {
        def validDataExist = [
            ['nombre de usuario':data.username],
            ['contraseña':data.password],
            ['emplado uuid':data.employeeUuid]
        ]
        def isDataExist = Utils.dataRequired(validDataExist, process, logId)
        if(isDataExist.status != 200) return isDataExist
        if (!data.username.specialCharacters()) {
            new Logs( process, "El nombre de usuario no coincide el formato esperado", logId, "ERROR", false, [  data: data.username ] )
            Utils.logger(logId, process, "El dato nombre de usuario no coincide el formato esperado", data.username)
            return TypeError.incorrectFormat( "nombre de usuario", "valor alfanúmerico", logId )
        }
        println data.businessEmail
        println !data.businessEmail.institutionalEmail()
        println !data.businessEmail.personalEmail()

        if (data.businessEmail && (!data.businessEmail.personalEmail())) {
            new Logs( process, "El correo electronico no coincide con el formato esperado", logId, "ERROR", false, [ data: data.businessEmail])
            Utils.logger(logId, process, "El correo electronico no coincide con el formato esperado", data.businessEmail)
            return TypeError.incorrectFormat("correo electronico", "correo electronico valido", logId)
        }
        def isValidUuid = Utils.validFormatUuid(process, "emplado uuid",data.employeeUuid, logId)
        if(isValidUuid.status != 200) return isValidUuid
        new Logs( process, "Validar datos de usuario", logId, "INFO", true, [ : ] )
        Utils.logger(logId, process, "Validar datos de usuario")
        return [data: [success: true], status:200]
    }
}