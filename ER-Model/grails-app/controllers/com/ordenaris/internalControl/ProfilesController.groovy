package com.ordenaris.internalControl


import grails.rest.*
import grails.converters.*

class ProfilesController {
	static responseFormats = ['json', 'xml']
	def ProfilesService
    def save(){
        def logId = new Logs("Registrar Perfil", "Incio de solicitud", request).getId()
        Utils.logger(logId, "Registrar Perfil", "Inicio de solicitud")
        def data = request.JSON
        
        def isValidData = validFormatData("Registrar Perfil", data, logId)
        if(isValidData.status !=200) return respond(isValidData.data, status:isValidData.status)
        
        def saveProfileResponse = ProfilesService.createProfile(data, logId)
        return respond(saveProfileResponse.data, status:saveProfileResponse.status)
        
    }
    
    def activate(){
        def logId = new Logs("Activar Perfil", "Inicio de solicitud", request).getId()
        Utils.logger(logId, "Activar Perfil", "Inicio de solicitud")
        def activateResponse = ProfilesService.activateProfile(params, logId)
        return respond(activateResponse.data, status:activateResponse.status)
    }
    
    def deactivate(){
        def logId = new Logs("Desactivar Perfil", "Inicio de solicitud", request).getId()
        Utils.logger(logId, "Desactivar Perfil", "Inicio de solicitud")
        def deactivateResponse = ProfilesService.deactivateProfile(params, logId)
        return respond(deactivateResponse.data, status:deactivateResponse.status)
    }
    
    
    def update(){
        def logId = new Logs("Editar Perfil", "Inicio de solicitud", request).getId()
        Utils.logger(logId,"Editar Perfil", "Inicio de solocitud")
        def data = request.JSON
        
        if(data.name && (!data.name.specialCharacters())){
            new Logs( process, "El dato 'name' ingresado no coincide con el formato esperado.", logId, "ERROR", false, [  data: data.name ] )
            Utils.logger(logId,process, "El dato 'name' ingresado no coincide con el formato esperado.", data.name)
            return TypeError.incorrectFormat( "'Nombre'", "Un valor alfanúmerico", logId )
        }
        
        def updateProfileResponse = ProfilesService.updateProfile(params, data, logId)
        return respond(updateProfileResponse.data, status:updateProfileResponse.status)
        
    }
    
    def delete(){
        def logId = new Logs("Eliminar Perfil", "Inicio de solicitud", request).getId()
        Utils.logger(logId, "Eliminar Perfil", "Inicio de solicitud")
        def deleteProfileResponse = ProfilesService.deleteProfile(params, logId)
        return respond(deleteProfileResponse.data, status:deleteProfileResponse.status)
    }
    
    def info(){
        def logId = new Logs("Información del Perfil", "Inicio de solicitud", request).getId()
        Utils.logger(logId,"Información del Perfil", "Inicio de solicitud")
        def infoProfileResponse = ProfilesService.infoProfile(params, logId)
        return respond(infoProfileResponse.data, status:infoProfileResponse.status)
    }
    
    def all(){
        def logId = new Logs("Lista de Perfiles", "Inicio de solicitud",request).getId()
        Utils.logger(logId, "Lista de Perfiles", "Inicio de solicitud")
        def allProfilesResponse = ProfilesService.allProfiles(params, logId)
        return respond(allProfilesResponse.data, status:allProfilesResponse.status)
    }
    
    
    
    def validFormatData(process, data, logId){
        def validDataExist = [
            ['Nombre':data.name],
            ['Descripcion': data.description]
        ]
        
        def isArrayExist = Utils.dataRequired(validDataExist, process, logId)
        if(isArrayExist.status != 200) return isArrayExist
        
        if (!data.name.specialCharacters()) {
            new Logs( process, "El dato 'name' ingresado no coincide con el formato esperado.", logId, "ERROR", false, [  data: data.name ] )
            Utils.logger(logId,process, "El dato 'name' ingresado no coincide con el formato esperado.", data.name)
            return TypeError.incorrectFormat( "'Nombre'", "Un valor alfanúmerico", logId )
        }
        
        return [data: [success:true], status:200]
    }
    
    
}
