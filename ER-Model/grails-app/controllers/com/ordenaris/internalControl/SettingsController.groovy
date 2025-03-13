package com.ordenaris.internalControl


import grails.rest.*
import grails.converters.*

class SettingsController {
	static responseFormats = ['json', 'xml']
	def SettingsService
    def responseHeader = request.getHeader("x-request-id")

    def save() {
        def data = request.JSON
        def logId = new Logs("Registrar valor parametrizable","Inicio de solicitud", request, responseHeader).getId()
        Utils.logger(logId,"Registrar valor parametrizable","Inicio de solicuitud")
        def validDataExist = [
            ['identificador': data.identifier],
            ['valor': data.data]
        ]
        def isDataExist = Utils.dataRequired(validDataExist, "Registrar valor parametrizable" , logId)
        if (isDataExist.status != 200) return respond(isDataExist.data, status:isDataExist.status) 
        def responseService= SettingsService.createSetting(data.value, logId)
        return respond(responseService.data, status:responseService.status)
    }

    // el uuid de settings es el identifier
    def update() {
        def data = request.JSON
        def logId = new Logs("Actualizar valor parametrizable", "Inicio de solicitud", request, responseHeader).getId()
        Utils.logger(logId, "Actualizar valor parametrizable", "Inicio de solicitud", params.uuid)
        def responseService = SettingsService.updateSetting(data.value, params.uuid, logId)
        return respond(responseService.data, status: responseService.status )
    }

    def delete() {
        def logId = new Logs("Eliminar valor parametrizable", "Inicio de solicitud", request, responseHeader).getId()
        Utils.logger(logId, "Eliminar valor parametrizable", "Inicio de solicitud", params.uuid)
        def responseService = SettingsService.deleteSetting(params.uuid, logId)
        return respond(responseService.data, status: responseService.status)
    }

    def refresh() {
        def logId = new Logs("Refrescar los valores parametrizables", "Inicio de solicitud", request, responseHeader).getId()
        Utils.logger(logId, "Refrescar los valores parametrizables", "Inicio de solicitud", params.uuid)
        def responseService = SettingsService.refreshSettings( logId)
        return respond(responseService.data, status: responseService.status)
    }
}
