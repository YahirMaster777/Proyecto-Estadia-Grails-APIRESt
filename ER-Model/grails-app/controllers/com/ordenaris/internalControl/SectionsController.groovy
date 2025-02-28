package com.ordenaris.internalControl


import grails.rest.*
import grails.converters.*

class SectionsController {
	static responseFormats = ['json', 'xml']
	def SectionsService
	
	def save(){
	    def logId = new Logs("Registrar Seccion", "Inicio de solicitud",request).getId()
	    Utils.logger(logId, "Registrar Seccion", "Inicio de solicitud")
	    def data = request.JSON
	    
	    def isValidData = validFormatData("Registrar Seccion", data, logId)
        if(isValidData.status != 200) return respond(isValidData.data, status:isValidData.status)
        
        def saveSectionResponse = SectionsService.createSection(data, logId)
        return respond(saveSectionResponse.data, status: saveSectionResponse.status)
	    
	}
	
	def activate(){
	    def logId = new Logs("Activar Seccion", "Inicio de solicitud", request).getId()
	    Utils.logger(logId, "Activar Seccion", "Inicio de solicitud")
	    def activeResponse = SectionsService.activateSection(params, logId)
	    return respond(activeResponse.data, status: activeResponse.status)
	}
	
	def deactivate(){
	    def logId = new Logs("Desactivar Seccion", "Inicio de solicitud", request).getId()
	    Utils.logger(logId, "Desactivar Seccion", "Inicio de solicitud")
	    def deactivateResponse = SectionsService.deactivateSection(params, logId)
	    return respond(deactivateResponse.data, status:deactivateResponse.status)
	}
	
	def validFormatData(process, data, logId){
        new Logs(process, "Validando los datos ingresados",logId, "INFO", true, [ : ])
        Utils.logger(logId,process, "Validando los datos ingresados")
        def validDataExist = [
            ['Nombre':data.name],
            ['Descripcion':data.description],
            ['Url':data.url]
        ]
        def isArrayExist = Utils.dataRequired(validDataExist,process,logId)
        if(isArrayExist.status != 200) return isArrayExist
    
        if(data.name && (!data.name.specialCharacters())){
            new Logs(process, "El dato 'name' no coincide con el formato esperado.", logId, "INFO", false, [ data:data.name ])
            Utils.logger(logId, process,"El dato 'name' no coincide con el formato esperado.", "Nombre: ${data.name}")
            return TypeError.incorrectFormat( "'Nombre'", "Un valor numerico", logId)
        }
        return [data:[success:true], status:200]
    }
}
