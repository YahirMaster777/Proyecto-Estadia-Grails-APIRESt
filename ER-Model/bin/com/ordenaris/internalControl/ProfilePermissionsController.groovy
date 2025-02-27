package com.ordenaris.internalControl


import grails.rest.*
import grails.converters.*

class ProfilePermissionsController {
	static responseFormats = ['json', 'xml']
	def ProfilePermissionsService
    def save(){
        def logId = new Logs("Registrar Permisos Perfil", "Inicio de solicitud",request).getId()
        Utils.logger(logId, "Registrar Permisos Perfil", "inicio de solicitud")
        def data = request.JSON
        def savePermissionsResponse = ProfilePermissionsService.createPermissions(data, logId)
        return respond(savePermissionsResponse.data, status:savePermissionsResponse.status)
    }
}
