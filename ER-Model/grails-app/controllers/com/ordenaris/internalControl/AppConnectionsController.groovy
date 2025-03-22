package com.ordenaris.internalControl


import grails.rest.*
import grails.converters.*

class AppConnectionsController {
	static responseFormats = ['json', 'xml']
	def AppConnectionsService
    def save(){
        def logId = new Logs("Registrar Conexiones de Apps", "Inicio de solicitud", request).getId()
        def data = request.JSON
        Utils.logger(logId,"Registrar Conexiones de Apps", "Inicio de solicitud")
        
        def isValidData = validFormatData("Registrar Conexiones de Apps", data, logId)
        if(isValidData.status != 200) return respond(isValidData.data, status:isValidData.status)
        
        def saveConectionResponse = AppConnectionsService.createConection(data, logId)
        return respond(saveConectionResponse.data, status: saveConectionResponse.status)
    }
    
    def update(){
        def logId = new Logs("Editar Conexion APP", "Inicio de solicitud", request).getId()
        Utils.logger(logId, "Editar Conexion APP", "Inicio de solicitud")
        def data = request.JSON
        def isValidData = validFormatData("Editar Conexion APP", data, logId)
        if(isValidData.status != 200) return respond(isValidData.data, status:isValidData.status)
        
        def updateConectionResponse = AppConnectionsService.updateConnection(params, data, logId)
        return respond(updateConectionResponse.data, status: updateConectionResponse.status)
    }
    
    
    def delete(){
        def logId = new Logs("Eliminar Conexion APP", "Inicio de solicitud", request).getId()
        Utils.logger(logId, "Eliminar Conexion APP", "Inicio de solicitud")
        def deleteConectionResponse = AppConnectionsService.deleteConnection(params, logId)
        return respond(deleteConectionResponse.data, status:deleteConectionResponse.status)
    }
    
    
    
    def validFormatData(process, data, logId){
        new Logs(process, "Validando los datos ingresados",logId, "INFO", true, [ : ])
        Utils.logger(logId,process, "Validando los datos ingresados")
        def validDataExist = [
            ['App':data.app],
            ['servicio':data.service],
        ]
        def isArrayExist = Utils.dataRequired(validDataExist,process,logId)
        if(isArrayExist.status != 200) return isArrayExist
    
        
        
        if(data.portApp && (!data.portApp.isPort())){
            new Logs(process, "El dato 'portApp' no coincide con el formato esperado.", logId, "INFO", false, [ data:data.portApp ])
            Utils.logger(logId, process,"El dato 'portApp' no coincide con el formato esperado.", "Puerto: ${data.portApp}")
            return TypeError.incorrectFormat( "'Puerto App'", "Un valor numerico", logId)
        }
        if(data.portService && (!data.portService.isPort())){
            new Logs(process, "El dato 'portService' no coincide con el formato esperado.", logId, "INFO", false, [ data:data.portService ])
            Utils.logger(logId, process,"El dato 'portService' no coincide con el formato esperado.", "Puerto: ${data.portService}")
            return TypeError.incorrectFormat( "'Puerto Service'", "Un valor numerico", logId)
        }
        
        return [data:[success:true], status:200]
        
    }
    
    
    
}
