package com.ordenaris.internalControl

import grails.rest.*
import grails.converters.*

class RecoveryController {
	static responseFormats = ['json', 'xml']
    def RecoveryService
	
    def createToken() {
        def data = request.JSON
        def logId = new Logs("Crear token", "Inicio de solicitud", request,  request.getHeader(Constants.HEADER_LOG_ID)).getId()
        Utils.logger(logId,"Crear token", "Inicio de solicitud")
        if(!Utils.validateAccessProject( request.getHeader(Constants.HEADER_ORD_SERVICE))) return respond(TypeError.noPermissions(logId))
        def isDataExist = Utils.dataRequired( [['nombre de usuario': data.username]], "Crear token" , logId)
        if (isDataExist.status != 200) return respond(isDataExist.data, status:isDataExist.status)
        if (!data.username.isInstitutionalEmail()) {
            new Logs( "Crear token", "El nombre de usuario no coincide con el formato esperado", logId, "ERROR", false, [  data: data.username ] )
            Utils.logger(logId, "Crear token", "El nombre de usuario no coincide con el formato esperado", data.username)
            def validUsernameResponse = TypeError.incorrectFormat( "nombre de usuario", "correo empresarial", logId )
            return respond(validUsernameResponse.data, status:validUsernameResponse.status)
        }
        def responseService = RecoveryService.createToken(data.username, logId, params.flag)
        return respond(responseService.data, status: responseService.status)
    }

    def resetPassword(){
        def data = request.JSON
        def process = params.flag?"Activar cuenta":"Recuperar constraseña"
        def logId = new Logs(process, "Inicio de solicitud", request,  request.getHeader(Constants.HEADER_LOG_ID)).getId()
        Utils.logger(logId,process, "Inicio de solicitud")
        if(!Utils.validateAccessProject( request.getHeader(Constants.HEADER_ORD_SERVICE))) return respond(TypeError.noPermissions(logId))
        def isDataExist = Utils.dataRequired( [['contraseña': data.password]], "Recuperar constraseña", logId)
        if (isDataExist.status != 200) return respond(isDataExist.data, status:isDataExist.status)
        if(!data.password.isPassword()) {
            new Logs( process, "La contraseña no coincide con el formato esperado ", logId, "ERROR", false, [  data: data.password ] )
            Utils.logger(logId, process, "La contraseña no coincide con el formato esperado", data.password)
            def validPasswordResponse = TypeError.incorrectFormat( "contraseña", "minimo 8 de caracteres, al menos una letra mayúscula, una letra minucula, un número, sin espacios y un caracter especial", logId )
            return respond(validPasswordResponse.data, status:validPasswordResponse.status)
        }
        def responseService = RecoveryService.resetPassword(data.password, params.uuid, params.flag, process, logId)
        return respond(responseService.data, status: responseService.status)
    }
}
