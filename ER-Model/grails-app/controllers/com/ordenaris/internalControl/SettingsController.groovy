package com.ordenaris.internalControl

import grails.rest.*
import grails.converters.*

class SettingsController {
	static responseFormats = ['json', 'xml']
	def SettingsService

    def save() {
        def data = request.JSON
        def logId = new Logs("Registrar valor parametrizable","Inicio de solicitud", request, request.getHeader(Constants.HEADER_LOG_ID)).getId()
        Utils.logger(logId,"Registrar valor parametrizable","Inicio de solicuitud")
        if(!Utils.validateAccessProject( request.getHeader(Constants.HEADER_WIKI_API))) return respond(TypeError.noPermissions(logId))
        def validDataExist = [
            ['identificador': data.identifier],
            ['valor': data.value]
        ]
        def isDataExist = Utils.dataRequired(validDataExist, "Registrar valor parametrizable" , logId)
        if (isDataExist.status != 200) return respond(isDataExist.data, status:isDataExist.status) 
        def responseService= SettingsService.createSetting(logId, data.identifier, data.value )
        return respond(responseService.data, status:responseService.status)
    }

    def update() {
        def data = request.JSON
        def logId = new Logs("Actualizar valor parametrizable", "Inicio de solicitud", request, request.getHeader(Constants.HEADER_LOG_ID)).getId()
        Utils.logger(logId, "Actualizar valor parametrizable", "Inicio de solicitud", params.uuid)
        def isDataExist = Utils.dataRequired([['valor': data.value]], "Actualizar valor parametrizable" , logId)
        if (isDataExist.status != 200) return respond(isDataExist.data, status:isDataExist.status) 
        if(!Utils.validateAccessProject( request.getHeader(Constants.HEADER_WIKI_API))) return respond(TypeError.noPermissions(logId))
        def responseService = SettingsService.updateSetting(logId, params.identifier, data.value)
        return respond(responseService.data, status: responseService.status )
    }

    def delete() {
        def logId = new Logs("Eliminar valor parametrizable", "Inicio de solicitud", request, request.getHeader(Constants.HEADER_LOG_ID)).getId()
        Utils.logger(logId, "Eliminar valor parametrizable", "Inicio de solicitud", params.uuid)
        if(!Utils.validateAccessProject( request.getHeader(Constants.HEADER_WIKI_API))) return respond(TypeError.noPermissions(logId))
        def responseService = SettingsService.deleteSetting(logId, params.identifier )
        return respond(responseService.data, status: responseService.status)
    }

    def refresh() {
        def logId = new Logs("Refrescar los valores parametrizables", "Inicio de solicitud", request, request.getHeader(Constants.HEADER_LOG_ID)).getId()
        Utils.logger(logId, "Refrescar los valores parametrizables", "Inicio de solicitud", params.uuid)
        if(!Utils.validateAccessProject( request.getHeader(Constants.HEADER_WIKI_API))) return respond(TypeError.noPermissions(logId))
        def responseService = SettingsService.refreshSettings(logId)
        return respond(responseService.data, status: responseService.status)
    }
}
