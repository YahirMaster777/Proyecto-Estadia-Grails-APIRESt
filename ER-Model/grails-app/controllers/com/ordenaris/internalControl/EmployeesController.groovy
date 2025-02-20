package com.ordenaris.internalControl


import grails.rest.*
import grails.converters.*

class EmployeesController {
	static responseFormats = ['json', 'xml']
	def EmployeesService
    def save() {
        def logId = new Logs("Registrar Empleado", "Inicio de solicitud", request).getId()
        def data = request.JSON   
        Utils.logger(logId, "Registrar Empleado", "Inicio de solicitud")
    
        def isValidData = validFormatData(data, logId)
        if (isValidData.status != 200) return respond(isValidData.data, status: isValidData.status)

        def saveEmployeeResponse = EmployeesService.createEmployee(data, logId)
        return respond(saveEmployeeResponse.data, status: saveEmployeeResponse.status)
    }
    
    def update(){
        def logId = new Logs("Editar Empleado", "Inicio de solicitud", request).getId()
        def data = request.JSON
        Utils.logger(logId, "Editar Empleado", "Inicio de solicitud")
        def updateEmployeeResponse = EmployeesService.updateEmployee(params,data,logId)
        return respond(updateEmployeeResponse.data, status:updateEmployeeResponse.status)
    }
    
    def delete(){
        def logId = new Logs("Eliminar Empleado","Inicio de solicitud", request).getId()
        Utils.logger(logId,"Eliminar Empleado", "Inicio de solicitud")
        def deleteEmployeeResponse = EmployeesService.deleteEmployee(params, logId)
        return respond(deleteEmployeeResponse.data, status:deleteEmployeeResponse.status)
    }
    
    
    def validFormatData(data, logId) {
        new Logs( "Validación de datos del usuario", "Validar datos de usuario", logId, "INFO", true, [ : ] )
        Utils.logger(logId, "Validación de datos del usuario", " Validar datos de usuario")
        def validDataExist = [
            ['nombre de usuario':data.username]
            // ['contraseña':data.password]
        ]
        def isArrayExist = Utils.validArrayExist(validDataExist, "usuario","dato", logId)
        if(isArrayExist.status != 200) return isArrayExist
        if (!data.username.specialCharacters()) {
            new Logs( "Validación de datos del usuario", "El dato nombre de usuario no coincide el formato esperado que se quiere ingresar.", logId, "ERROR", false, [  data: data.username ] )
            Utils.logger(logId,"Validación de datos del usuario", "No coincide el formato esperado que se quiere ingresar.", data.username)
            return TypeError.incorrectFormat( "nombre de usuario", "valor alfanúmerico", logId )
        }
        new Logs( "Validación de datos del usuario", "Control de la información de usuario", logId, "INFO", true, [ data: data, uuid: logId ] )
        Utils.logger(logId,"Validación de datos del usuario", "Control de la información de usuario", "Los datos cumplen con los valores esperados")
        return [data: [success: true], status:200]
    }
    
    
    
    
}
