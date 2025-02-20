package com.ordenaris.internalControl


import grails.rest.*
import grails.converters.*

class AppsController {
	static responseFormats = ['json', 'xml']
    def AppsService
    
    def save(){
        def logId = new Logs("Registrar Aplicacion", "Inicio de solicitud", request).getId()
        Utils.logger(logId, "Registrar Aplicacion", "Inicio de solicitud")
        def data = request.JSON
        def isValidData = validFormatData(data, logId)
        if(isValidData.status != 200) return respond(isValidData.data, status:isValidData.status)
        
        def saveAppResponse = AppsService.createApp(data, logId)
        return respond(saveAppResponse.data,status: saveAppResponse.status)
    
    }
    
    def validFormatData(data, logId) {
        new Logs( "Registrar Aplicacion", "Validando los datos ingresados", logId, "INFO", true, [ : ] )
        Utils.logger(logId, "Registrar Aplicacion", "Validando los datos ingresados")
        def validDataExist = [
            ['Criticidad':data.criticality],
            ['Nombre':data.name],
            ['Tipo' : data.type],
         
           
        ]
        def isArrayExist = Utils.validArrayExist(validDataExist, "Aplicacion","datos", logId)
        if(isArrayExist.status != 200) return isArrayExist
        if (!data.name.specialCharacters()) {
            new Logs( "Registrar Aplicacion", "El dato nombre de Aplicacion no coincide con el formato esperado.", logId, "ERROR", false, [  data: data.name ] )
            Utils.logger(logId,"Registrar Aplicacion", "No coincide con el formato esperado.", data.name)
            return TypeError.incorrectFormat( "Nombre de Aplicacion", "valor alfanúmerico", logId )
        }
        if (!data.type.specialCharacters()) {
            new Logs( "Registrar Aplicacion", "El dato type de Aplicacion no coincide con el formato esperado.", logId, "ERROR", false, [  data: data.type ] )
            Utils.logger(logId,"Registrar Aplicacion", "No coincide con el formato esperado.", data.type)
            return TypeError.incorrectFormat( "type de Aplicacion", "valor alfanúmerico", logId )
        }
        if (!data.criticality.specialCharacters()) {
            new Logs( "Registrar Aplicacion", "El dato criticality de Aplicacion no coincide con el formato esperado.", logId, "ERROR", false, [  data: data.criticality ] )
            Utils.logger(logId,"Registrar Aplicacion", "No coincide con el formato esperado.", data.criticality)
            return TypeError.incorrectFormat( "criticality de Aplicacion", "valor alfanúmerico", logId )
        }
        
        
        
        
        new Logs( "Registrar Aplicacion", "Control de la información de Aplicacion", logId, "INFO", true, [ data: data, uuid: logId ] )
        Utils.logger(logId,"Registrar Aplicacion", "Control de la información de Aplicacion", "Los datos cumplen con los valores esperados")
        return [data: [success: true], status:200]
    }
    
	
	
	

	
}
