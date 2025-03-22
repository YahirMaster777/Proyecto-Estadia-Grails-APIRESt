package com.ordenaris.internalControl


import grails.rest.*
import grails.converters.*

class AppsController {
	static responseFormats = ['json', 'xml']
    def AppsService
    
    @PermissionRequired("create_app")
    def save(){
        def logId = new Logs("Registrar Aplicacion", "Inicio de solicitud", request).getId()
        Utils.logger(logId, "Registrar Aplicacion", "Inicio de solicitud")
        def data = request.JSON
        def isValidData = validFormatData("Registrar Aplicacion",data, logId)
        if(isValidData.status != 200) return respond(isValidData.data, status:isValidData.status)
        
        def saveAppResponse = AppsService.createApp(data, logId)
        return respond(saveAppResponse.data, status: saveAppResponse.status)
    }
    
    def delete(){
        def logId = new Logs("Eliminar Aplicacion", "Inicio de solicitud", request).getId()
        Utils.logger(logId, "Eliminar Aplicacion", "Inicio de solicitud")
        def deleteAppResponse = AppsService.deleteApp(params, logId)
        return respond(deleteAppResponse.data, status: deleteAppResponse.status)
    }
    
    def changeStatus(){
        def logId = new Logs("Status Aplicacion", "Inicio de solicitud", request).getId()
        Utils.logger(logId, "Status Aplicacion", "Inicio de solicitud")
        def activeAppResponse = AppsService.statusManagement(params, logId)
        return respond(activeAppResponse.data, status:activeAppResponse.status)
    }
    

    def info(){
        def logId = new Logs("Informacion de Aplicacion", "Inicio de solicitud", request).getId()
        Utils.logger(logId,"Informacion de Aplicacion","Inicio de solicitud")
        def infoAppResponse = AppsService.infoApp(params, logId)
        return respond(infoAppResponse.data, status:infoAppResponse.status)
    }
    
    
    
    def all(){
        def logId = new Logs( "Lista de Aplicaciones", "Iniciando solicitud", request).getId()
        Utils.logger(logId,"Lista de Aplicaciones","Inicio de solicitud")
        def apps = AppsService.allApps(logId)
        return respond(apps.data , status:apps.status)
    }
    
    def list(){
        
    }
    
    
    
    def validFormatData(process, data, logId) {
        def validDataExist = [
            ['Criticidad':data.criticality],
            ['Nombre':data.name],
            ['Tipo' : data.type]
        ]
        def isArrayExist = Utils.dataRequired(validDataExist,process,logId)
        if(isArrayExist.status != 200) return isArrayExist
        
        new Logs( process, "Validando los datos ingresados", logId, "INFO", true, [ : ])
        Utils.logger(logId,process, "Validando los datos ingresados")
        
        def listStatus=['activa','Depracada','pendiente','desarollo']
        if(data.status && (listStatus.indexOf(data.status) < 0)){
            new Logs( process, "El dato 'status' ingresado no coincide con el formato esperado", logId, "ERROR", false, [  data: data.status ] )
            Utils.logger(logId,process, "El dato 'status' ingresado no coincide con el formato esperado", data.status)
            return TypeError.incorrectFormat( "'Status de la Aplicacion'", ": 'activa','Depracada','pendiente','desarollo'", logId )
        }
        
        if (!data.name.isSpecialCharacters()) {
            new Logs( process, "El dato 'name' ingresado no coincide con el formato esperado.", logId, "ERROR", false, [  data: data.name ] )
            Utils.logger(logId,process, "El dato 'name' ingresado no coincide con el formato esperado.", data.name)
            return TypeError.incorrectFormat( "'Nombre'", "Un valor alfanúmerico", logId )
        }
       
        
        def listType = ['frontend','backend','aplication','base de datos']
        if ((listType.indexOf(data.type) < 0) || (!data.type.isSpecialCharacters())){
            new Logs( process, "El dato 'type' ingresado no coincide con el formato esperado", logId, "ERROR", false, [  data: data.type ] )
            Utils.logger(logId,process, "El dato 'type' ingresado no coincide con el formato esperado", data.type)
            return TypeError.incorrectFormat( "'Tipo de Aplicacion'", ": 'frontend','backend','aplication','base de datos'", logId )
        }
                
        
        def listCriticality = ['indiferente','baja','media','alta','critica']
        if ((listCriticality.indexOf(data.criticality) < 0) || (!data.criticality.isSpecialCharacters())){
            new Logs( process, "El dato 'criticality' ingresado no coincide con el formato esperado", logId, "ERROR", false, [  data: data.criticality ] )
            Utils.logger(logId,process, "El dato 'criticality' ingresado no coincide con el formato esperado", data.criticality)
            return TypeError.incorrectFormat( "'Criticidad'", ": 'indiferente','baja','media','alta','critica'", logId )
        }
        
        return [data: [success: true], status:200]
    }
    
	
}
