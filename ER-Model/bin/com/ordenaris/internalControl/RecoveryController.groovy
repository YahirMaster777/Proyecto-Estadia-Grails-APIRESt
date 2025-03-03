package com.ordenaris.internalControl

import grails.rest.*
import grails.converters.*
// import grails.web.context.ServletContextHolder

class RecoveryController {
	static responseFormats = ['json', 'xml']
    def RecoveryService
    def responseHeader = request.getHeader("x-request-id")
    def minExpired = servletContext.getAttribute('MINUTES_OF_VALIDITY_CODE').toInteger()
    def numberIntents = servletContext.getAttribute('NUMBER_OF_RECOVERY_ATTEMPTS').toInteger()
	
    def createToken() {
        def data = request.JSON
        def logId = new Logs("Crear token", "Inicio de solicitud", request, responseHeader).getId()
        Utils.logger(logId,"Crear token", "Inicio de solicitud")
        def isDataExist = Utils.dataRequired( [['nombre de usuario': data.username]], "Crear token" , logId)
        if (isDataExist.status != 200) return respond(isDataExist.data, status:isDataExist.status)
        if (!data.username.institutionalEmail()) {
            new Logs( "Crear token", "El nombre de usuario no coincide con el formato esperado", logId, "ERROR", false, [  data: data.username ] )
            Utils.logger(logId, "Crear token", "El nombre de usuario no coincide con el formato esperado", data.username)
            def validUsernameResponse = TypeError.incorrectFormat( "nombre de usuario", "correo empresarial", logId )
            return respond(validUsernameResponse.data, status:validUsernameResponse.status)
        }
        def responseService = RecoveryService.createToken(data.username, minExpired, numberIntents, logId)
        return respond(responseService.data, status: responseService.status)
    }

    def resetPassword(){
        def data = request.JSON
        def logId = new Logs("Recuperar constraseña", "Obteniendo la nueva contraseña", request, responseHeader).getId()
        Utils.logger(logId,"Recuperar constraseña", "Obteniendo la nueva contraseña")
        def isDataExist = Utils.dataRequired( [['contraseña': data.password]], "Recuperar constraseña", logId)
        if (isDataExist.status != 200) return respond(isDataExist.data, status:isDataExist.status)
        if(!data.password.validPassword()) {
            new Logs( "Recuperar constraseña", "La contraseña no coincide con el formato esperado ", logId, "ERROR", false, [  data: data.password ] )
            Utils.logger(logId, "Recuperar constraseña", "La contraseña no coincide con el formato esperado", data.password)
            def validPasswordResponse = TypeError.incorrectFormat( "contraseña", "minimo 8 de caracteres, al menos una letra mayúscula, una letra minucula, un número, sin espacios y un caracter especial", logId )
            return respond(validPasswordResponse.data, status:validPasswordResponse.status)
        }
        def responseService = RecoveryService.resetPassword(data.password, params.uuid, numberIntents, logId)
        return respond(responseService.data, status: responseService.status)
    }

    def activateAccount() {
        def data = request.JSON
        def logId = new Logs("Activar cuenta", "Obteniendo la nueva contraseña", request, responseHeader).getId()
        Utils.logger(logId,"Activar cuenta", "Obteniendo la nueva contraseña")
        def validDataExist = [
            ['nombre de usuario':data.username],
            ['contraseña':data.password]
        ]
        def isDataExist = Utils.dataRequired( validDataExist , "Activar cuenta" , logId)
        if (isDataExist.status != 200) return respond(isDataExist.data, status:isDataExist.status)
        
    }
}
