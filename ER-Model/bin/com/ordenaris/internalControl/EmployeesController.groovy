package com.ordenaris.internalControl


import grails.rest.*
import grails.converters.*

class EmployeesController {
	static responseFormats = ['json', 'xml']
	
    def save(){}
    //     def logId = new Logs("Registrar","Inicio de solicitud", request).getId()
    //     def data = request.JSON
    //     Utils .logger(logId,"Registrar","Inicio de solicuitud")
    //     def isValidData = validFormatData(data, logId)
    //     if (isValidData.status != 200) return respond(isValidData.data, status: isValidData.status )
        
        
    // }
    
    // def validDataExist = [
    //         ['latitud': data.latitude],
    //         ['longitud': data.length],
    //         ['calle': data.street],
    //         ['télefono': data.phone],
    //         ['colonia': data.cologne],
    //         ['locacion': data.location],
    //         ['nombre': data.name]
    //     ]
    //     def isArrayExist = Utils.validArrayExist(validDataExist,"consultorio", "dato", logId)
    //     if(isArrayExist.status != 200) return isArrayExist
    
}
