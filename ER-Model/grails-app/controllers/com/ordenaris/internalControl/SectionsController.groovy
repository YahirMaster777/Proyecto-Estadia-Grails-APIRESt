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
	    def validDataExist = [
            ['Nombre':data.name],
            ['Descripcion':data.description],
            ['Url':data.url]
        ]
	    def isArrayExist = Utils.dataRequired(validDataExist,"Registrar Seccion",logId)
        if(isArrayExist.status != 200) return respond(isArrayExist.data, status:isArrayExist.status)
        
	    def isValidData = validFormatData("Registrar Seccion", data, logId)
        if(isValidData.status != 200) return respond(isValidData.data, status:isValidData.status)
        
        def saveSectionResponse = SectionsService.createSection(data, logId)
        return respond(saveSectionResponse.data, status: saveSectionResponse.status)
	    
	}
	
	def update(){
		def logId = new Logs("Actualizar Seccion", "Inicio de solicitud", request).getId()
		Utils.logger(logId, "Actualizar Seccion", "Inicio de solicitud")
		def data = request.JSON
		
		def isValidData = validFormatData("Actualizar usuario",data, logId)
        if (isValidData.status != 200) return respond(isValidData.data, status: isValidData.status )
		
		def updateSectionResponse = SectionsService.updateSection(params, data, logId)
		return respond(updateSectionResponse.data, status:updateSectionResponse.status)
		
	}
	
	
	def changeStatus(){
		def logId = new Logs("Actualizar status", "Inicio de solicitud", request).getId()
		Utils.logger(logId, "Actualizar status", "Inicio de solicitud")
		def changeStatusResponse = SectionsService.updateStatus(params, logId)
		return respond(changeStatusResponse.data, status:changeStatusResponse.status)
	}
	

	
	def validFormatData(process, data, logId){
        new Logs(process, "Validando los datos ingresados",logId, "INFO", true, [ : ])
        Utils.logger(logId,process, "Validando los datos ingresados")
     
    
        def listStatus  = ['Activa','Inactiva','Mantenimiento','Pruebas']
        if(data.status && ((listStatus.indexOf(data.status) < 0))){
            new Logs(process, "El dato 'status' no coincide con el formato esperado.", logId, "INFO", false, [ : ])
            Utils.logger(logId, process,"El dato 'status' no coincide con el formato esperado")
            return TypeError.incorrectFormat( "'Status'", ": 'Activa','Inactiva','Mantenimiento','Pruebas'", logId)
        }
        
        if(data.name && (!data.name.specialCharacters())){
            new Logs(process, "El dato 'name' no coincide con el formato esperado.", logId, "INFO", false, [ data:data.name ])
            Utils.logger(logId, process,"El dato 'name' no coincide con el formato esperado.", "Nombre: ${data.name}")
            return TypeError.incorrectFormat( "'Nombre'", "Un valor numerico", logId)
        }
        
        return [data:[success:true], status:200]
    }
}
