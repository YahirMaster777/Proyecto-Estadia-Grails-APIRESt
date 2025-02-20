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
        def isValidData = validFormatData(data, logId)
        if (isValidData.status != 200) return respond(isValidData.data, status: isValidData.status)
        def responseService= UsersService.createUser(data, logId)
        return respond(responseService.data, status:responseService.status)
    }

    def  update() {
        def data = request.JSON
        def logId = new Logs("Actualizar usuario", "Inicio de solicitud", request, responseHeader).getId()
        Utils.logger(logId, "Actualizar usuario", "Inicio de solicitud", params.uuid)
        def isValidData = validFormatData(data, logId)
        if (isValidData.status != 200) return respond(isValidData.data, status: isValidData.status )
        def responseService = UsersService.updateUser(data, params.uuid, logId)
        return respond(responseService.data, status: responseService.status )
    }

    def read() {
        def logId = new Logs("Buscar usuario", "Inicio de solicitud", request, responseHeader).getId()
        Utils.logger(logId, "Buscar usuario", "Inicio de solicitud", params.uuid)
        def responseService = UsersService.readUser(params.uuid, logId)
        return respond( responseService.data, status: responseService.status)
    }

    def delete() {
        def logId = new Logs("Eliminar usuario", "Inicio de solicitud", request, responseHeader).getId()
        Utils.logger(logId, "Eliminar usuario", "Inicio de solicitud", params.uuid)
        def responseService = UsersService.deleteUser(params.uuid, logId)
        return respond(responseService.data, status: responseService.status)
    }

    def list() {
        def logId = new Logs("Páginado usuario", "Inicio de solicitud", request, responseHeader).getId()
        Utils.logger(logId, "Páginado usuario","Inicio de solicitud", params)
        def isValidParams = Utils.validFormatParams(params, "usuario", ["username", "password", "businessEmail"] , logId)
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

    def validFormatData(data, logId) {
        new Logs( "Validación de datos del usuario", "Validar datos de usuario", logId, "INFO", true, [ : ] )
        Utils.logger(logId, "Validación de datos del usuario", " Validar datos de usuario")
        def validDataExist = [
            ['nombre de usuario':data.username]
            // ['contraseña':data.password]
        ]
        def isArrayExist = Utils.dataRequired(validDataExist, "usuario","dato", logId)
        if(isArrayExist.status != 200) return isArrayExist
        if (!data.username.specialCharacters()) {
            new Logs( "Validación de datos del usuario", "El dato nombre de usuario no coincide el formato esperado que se quiere ingresar.", logId, "ERROR", false, [  data: data.username ] )
            Utils.logger(logId,"Validación de datos del usuario", "No coincide el formato esperado que se quiere ingresar.", data.username)
            return TypeError.incorrectFormat( "nombre de usuario", "valor alfanúmerico", logId )
        }
        // if (!data.password.specialCharacters()) {
        //     new Logs( "Validación de datos del usuario", "El dato contraseña no coincide el formato esperado que se quiere ingresar.", logId, "ERROR", false, [  data: data.password ] )
        //     Utils.logger(logId,"Validación de datos del usuario", "No coincide el formato esperado que se quiere ingresar.", data.password)
        //     return TypeError.incorrectFormat( "contraseña", "valor alfanúmerico", logId )
        // }
        // TODO: Validar el correo con espresion regular
        // TODO: Validar el uuid empleado
        new Logs( "Validación de datos del usuario", "Control de la información de usuario", logId, "INFO", true, [ data: data, uuid: logId ] )
        Utils.logger(logId,"Validación de datos del usuario", "Control de la información de usuario", "Los datos cumplen con los valores esperados")
        return [data: [success: true], status:200]
    }
}