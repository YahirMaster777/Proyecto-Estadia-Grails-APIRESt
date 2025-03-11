package com.ordenaris.internalControl


import grails.rest.*
import grails.converters.*

class ServersController {
	static responseFormats = ['json', 'xml']
    def ServersService
    def responseHeader = request.getHeader("x-request-id")
	
    def save() { 
        def data = request.JSON
        def logId = new Logs("Registrar servidor","Inicio de solicitud", request, responseHeader).getId()
        Utils.logger(logId,"Registrar servidor","Iniciio de solicitud")
        def isValidData = validFormatData("Actualizar usuario",data, logId)
        if (isValidData.status != 200) return respond(isValidData.data, status: isValidData.status )
        def responseService = UsersService.updateUser(data, params.uuid, logId)
        return respond(responseService.data, status: responseService.status )
    }

    def update() {
        def data = request.JSON
        def logId = new Logs("Actualizar servidor","Inicio de solicitud", request, responseHeader).getId()
        Utils.logger(logId,"Actualizar servidor","Iniciio de solicitud")
        def isValidData = validFormatData("Actualizar servidor",data, logId)
        if (isValidData.status != 200) return respond(isValidData.data, status: isValidData.status )
        def responseService = UsersService.updateUser(data, params.uuid, logId)
        return respond(responseService.data, status: responseService.status )
    }

    def info() {
        def logId = new Logs("Buscar servidor", "Inicio de solicitud", request).getId()
        Utils.logger(logId,"Buscar servidor","Inicio de solicitud", params.uuid)
        def infoAppResponse = ServersService.readServer(params.uuid, logId)
        return respond(infoAppResponse.data, status:infoAppResponse.status)
    }

    def delete() { 
        def logId = new Logs("Eliminar servidor", "Inicio de solicitud", request, responseHeader).getId()
        Utils.logger(logId, "Eliminar servidor", "Inicio de solicitud", params.uuid)
        def responseService = UsersService.deleteUser(params.uuid, logId)
        return respond(responseService.data, status: responseService.status)
    }

    def activate() { 
        def logId = new Logs("Activar servidor", "Inicio de solicitud", request).getId()
        Utils.logger(logId, "Activar servidor", "Inicio de solicitud", params.uuid)
        def responseService = ServersService.activeServer(params.uuid, logId)
        return respond(responseService.data, status:responseService.status)
    }

    def deactivate() { 
        def logId = new Logs("Desactivar servidor","Inicio de solicitud", request).getId()
        Utils.logger(logId,"Desactivar servidor", "Inicio de solicitud", params.uuid)
        def responseService = ServersService.deactivateServer(params.uuid, logId)
        return respond(responseService.data, status:responseService.status)
    }
    def list() { 
        
    }

    def all() { 
        def logId = new Logs("Lista de servicores", "Inicio de solicitud", request, responseHeader).getId()
        Utils.logger(logId,"Lista de servicores", "Inicio de solicitud")
        def responseService = ServersService.allServer( logId)
        return respond(responseService.data, status: responseService.status)
    }

    def validFormatData(process, data, logId){
        def validDataExist = [
            ['almacenamiento': data.storage],
            ['fecha de adquisición': data.dateAcquisition],
            ['proovedor de nube': data.cloudProvider],
            ['ubicación': data.location],
            ['fecha de despliegue': data.dateDeploy],
            ['dirección mac': data.macAddress],
            ['capacidad de provesamiento': data.processingCapacity],
            ['ubicación de acceso': data.host],
            ['memoria': data.memory],
            ['tipo de servidor': data.type],
            ['sistema operativo': data.operatingSystem],
        ]
        if (data.macAddress && data.macAddress.macAddress()) {
            new Logs( process, "La dirección mac no coincide con el formato esperado", logId, "ERROR", false, [  data: data.macAddress ] )
            Utils.logger(logId, process, "La dirección mac no coincide con el formato esperado", data.macAddress)
            return TypeError.incorrectFormat( "dirección mac", "correo empresarial", logId )
        }
        // if (data.macAddress && data.macAddress.macAddress()) {
        //     new Logs( process, "La dirección mac no coincide con el formato esperado", logId, "ERROR", false, [  data: data.macAddress ] )
        //     Utils.logger(logId, process, "La dirección mac no coincide con el formato esperado", data.macAddress)
        //     return TypeError.incorrectFormat( "dirección mac", "correo empresarial", logId )
        // }

    }
}