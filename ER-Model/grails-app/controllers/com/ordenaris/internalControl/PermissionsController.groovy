package com.ordenaris.internalControl


import grails.rest.*
import grails.converters.*

class PermissionsController {
	static responseFormats = ['json', 'xml']
	def PermissionsService
	
    def save(){
        def logId = new Logs("Registrar Permiso", "Inicio de solicitud", request).getId()
        Utils.logger(logId, "Registrar Permiso", "Inicio de solicitud")
        def data = request.JSON
        def validDataExist = [
            ['Nombre':data.name],
            ['Descripcion': data.description],
            ['Alias': data.alias],
            ['Seccion':data.section]
        ]

        def isArrayExist = Utils.dataRequired(validDataExist,"Registrar Permiso",logId)
        if(isArrayExist.status != 200) return respond(isArrayExist.data, status:isArrayExist.status)
        
	    def isValidData = validFormatData("Registrar Permiso", data, logId)
        if(isValidData.status != 200) return respond(isValidData.data, status:isValidData.status)
        
            
        def savePermissionResponse = PermissionsService.createPermission(data, logId)
        return respond(savePermissionResponse.data, status:savePermissionResponse.status)
    }
    
    def validFormatData(process, data, logId){
        new Logs(process, "Validando los datos ingresados",logId, "INFO", true, [ : ])
        Utils.logger(logId,process, "Validando los datos ingresados")
        
        
        
        if(data.name && (!data.name.specialCharacters())){
            new Logs(process, "El dato 'name' no coincide con el formato esperado.", logId, "INFO", false, [ data:data.name ])
            Utils.logger(logId, process,"El dato 'name' no coincide con el formato esperado.", "Nombre: ${data.name}")
            return TypeError.incorrectFormat( "'Nombre'", "Un valor numerico", logId)
        }
        
        return [data:[success:true], status:200]
    }
}
