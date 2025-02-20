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
        new Logs( "Validación de datos del empleado", "Validar datos de empleado", logId, "INFO", true, [ : ] )
        Utils.logger(logId, "Validación de datos del empleado", " Validar datos de empleado")
        def validDataExist = [
            ['Curp':data.curp],
            ['Phone':data.phone],
            ['IdEmpleado':data.idEmployee],
            ['Rfc':data.rfc],
            ['lastName1':data.lastName1],
            ['lastName2':data.lastName2],
            ['name':data.name],
            ['nss' : data.nss],
            ['position':data.position],
            ['company':data.company],           
            ['personalEmail':data.personalEmail]    
        ]
        def isDataExist = Utils.validArrayExist(validDataExist, "empleado","dato", logId)
        if(isDataExist.status != 200) return isDataExist
        if (!data.name.specialCharacters()) {
            new Logs( "Validación de datos del empleado", "El dato nombre de empleado no coincide el formato esperado que se quiere ingresar.", logId, "ERROR", false, [  data: data.name ] )
            Utils.logger(logId,"Validación de datos del empleado", "No coincide el formato esperado que se quiere ingresar.", data.name)
            return TypeError.incorrectFormat( "Nombre de empleado", "valor alfanúmerico", logId )
        }
        if (!data.curp.specialCharacters()) {
            new Logs( "Validación de datos del empleado", "El dato Curp de empleado no coincide el formato esperado que se quiere ingresar.", logId, "ERROR", false, [  data: data.curp ] )
            Utils.logger(logId,"Validación de datos del empleado", "No coincide el formato esperado que se quiere ingresar.", data.curp)
            return TypeError.incorrectFormat( "Curp de empleado", "valor alfanúmerico", logId )
        }
        if (!data.rfc.specialCharacters()) {
            new Logs( "Validación de datos del empleado", "El dato RFC de empleado no coincide el formato esperado que se quiere ingresar.", logId, "ERROR", false, [  data: data.rfc ] )
            Utils.logger(logId,"Validación de datos del empleado", "No coincide el formato esperado que se quiere ingresar.", data.rfc)
            return TypeError.incorrectFormat( "RFC de empleado", "valor alfanúmerico", logId )
        }
        if (!data.nss.validNss()) {
            new Logs( "Validación de datos del empleado", "El dato NSS de empleado no coincide el formato esperado que se quiere ingresar.", logId, "ERROR", false, [  data: data.nss ] )
            Utils.logger(logId,"Validación de datos del empleado", "No coincide el formato esperado que se quiere ingresar.", data.nss)
            return TypeError.incorrectFormat( "NSS de empleado", "valor alfanúmerico", logId )
        }
        
        if (!data.phone.phoneNumber()) {
            new Logs( "Validación de datos del empleado", "El dato phone de empleado no coincide el formato esperado que se quiere ingresar.", logId, "ERROR", false, [  data: data.phone ] )
            Utils.logger(logId,"Validación de datos del empleado", "No coincide el formato esperado que se quiere ingresar.", data.phone)
            return TypeError.incorrectFormat( "telefono del empleado", "valor alfanúmerico", logId )
        }
        new Logs( "Validación de datos del empleado", "Control de la información de empleado", logId, "INFO", true, [ data: data, uuid: logId ] )
        Utils.logger(logId,"Validación de datos del empleado", "Control de la información de empleado", "Los datos cumplen con los valores esperados")
        return [data: [success: true], status:200]
    }
    
    
    
    
}
