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
        def validDataExist = [
            ['nombre de usuario':data.username],
            ['contraseña':data.password],
            ['emplado uuid':data.employeeUuid]
        ]
        def isDataExist = Utils.dataRequired(validDataExist, "Registrar usuario" , logId)
        if (isDataExist.status != 200) return respond(isDataExist.data, status:isDataExist.status)
        if(!data.password.validPassword()) {
            new Logs( "Registrar usuario", "La contraseña no coincide con el formato esperado ", logId, "ERROR", false, [  data: data.password ] )
            Utils.logger(logId, "Registrar usuario", "La contraseña no coincide con el formato esperado", data.password)
            def validPasswordResponse = TypeError.incorrectFormat( "contraseña", "minimo 8 de caracteres, al menos una letra mayúscula, una letra minucula, un número, sin espacios y un caracter especial", logId )
            return respond(validPasswordResponse.data, status:validPasswordResponse.status)
        }
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
        def isValidParams = Utils.validFormatParams(params, "usuario", ["username", "businessEmail"] , logId)
        if (isValidParams.status != 200) return respond(isValidParams.data, status: isValidParams.status )
        def responseService = UsersService.listUser(params, logId)
        return respond(responseService.data, status: responseService.status)
    }
    
    def all() {
        def logId = new Logs("Lista de usuarios", "Inicio de solicitud", request, responseHeader).getId()
        Utils.logger(logId,"Lista de usuarios", "Inicio de solicitud")
        def responseService = UsersService.allUser( logId)
        return respond(responseService.data, status: responseService.status)
    }

    def validFormatData(process,data, logId) {
        if (data.username  && !data.username.institutionalEmail()) {
            new Logs( process, "El nombre de usuario no coincide con el formato esperado", logId, "ERROR", false, [  data: data.username ] )
            Utils.logger(logId, process, "El nombre de usuario no coincide con el formato esperado", data.username)
            return TypeError.incorrectFormat( "nombre de usuario", "correo empresarial", logId )
        }
        if (data.businessEmail && (!data.businessEmail.institutionalEmail())) {
            new Logs( process, "El correo electronico no coincide con el formato esperado", logId, "ERROR", false, [ data: data.businessEmail])
            Utils.logger(logId, process, "El correo electronico no coincide con el formato esperado", data.businessEmail)
            return TypeError.incorrectFormat("correo electronico", "correo electronico valido", logId)
        }
        def userList = Users.createCriteria().list() {
            or{
                ilike('username', data.username?:'')
                ilike('businessEmail', data.businessEmail?:'')
            }   
        }.collect()
        if (!userList.isEmpty()) {
            new Logs(process, "Daxos existentes, por favor utilice valores diferentes", logId, "INFO", false, [data: [success: false]])
            Utils.logger(logId, process, "Daxos existentes, por favor utilice valores diferentes" )
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