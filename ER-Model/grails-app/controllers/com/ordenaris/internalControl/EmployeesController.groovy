package com.ordenaris.internalControl

import grails.rest.*
import grails.converters.*

class EmployeesController {
	static responseFormats = ['json', 'xml']
	def EmployeesService

    // TODO: En la carpeta de usuarios guardar todos las fotos de perfil con el nombre del uuid del usuario
    def save() {
        def data = request.JSON   
        def logId = new Logs("Registrar Empleado", "Inicio de solicitud", request, request.getHeader(Constants.HEADER_LOG_ID)).getId()
        Utils.logger(logId, "Registrar Empleado", "Inicio de solicitud")
        if(!Utils.validateAccessProject( request.getHeader(Constants.HEADER_ORD_SERVICE))) return respond(TypeError.noPermissions(logId))
        def validDataExist = [
            ['Curp':data.curp],
            ['Teléfono':data.phone],
            ['RFC':data.rfc],
            ['apellido paterno':data.lastName1],
            ['apellido materno':data.lastName2],
            ['nombre':data.name],
            ['NSS' : data.nss],
            ['uuid posición':data.uuidPosition],
            ['uuid company':data.uuidCompany],           
            ['correo electronico personal':data.personalEmail]  
        ]
        if(params.account){
            validDataExist.add( ['Lista de permisos':data.hashMApPermissions])
            validDataExist.add( ['Nombre de usuario':data.username])
        }
        def isDataExist = Utils.dataRequired(validDataExist, "Registrar empleado" , logId)
        if (isDataExist.status != 200) return respond(isDataExist.data, status:isDataExist.status)
        def isValidData = validFormatData("Registrar Empleado", data, logId)
        if (isValidData.status != 200) return respond(isValidData.data, status: isValidData.status)
        def saveEmployeeResponse = EmployeesService.createEmployee(data, params.account, params.activation, logId)
        return respond(saveEmployeeResponse.data, status: saveEmployeeResponse.status)
    }
    
    def update(){
        def data = request.JSON
        def logId = new Logs("Editar Empleado", "Inicio de solicitud", request, request.getHeader(Constants.HEADER_LOG_ID)).getId()
        Utils.logger(logId, "Editar Empleado", "Inicio de solicitud")
        if(!Utils.validateAccessProject( request.getHeader(Constants.HEADER_ORD_SERVICE))) return respond(TypeError.noPermissions(logId))
        def updateEmployeeResponse = EmployeesService.updateEmployee(params,data,logId)
        return respond(updateEmployeeResponse.data, status:updateEmployeeResponse.status)
    }

    def accountManagement(){
        def data = request.JSON
        def logId = new Logs("Gestion de cuenta de empleado","Inicio de solicitud", request, request.getHeader(Constants.HEADER_LOG_ID)).getId()
        Utils.logger(logId,"Gestion de cuenta de empleado", "Inicio de solicitud")
        if(!Utils.validateAccessProject( request.getHeader(Constants.HEADER_ORD_SERVICE))) return respond(TypeError.noPermissions(logId))
        if (params.activation){
            def validDataExist = [['Nombre de usuario':data.username]]
            validDataExist.add(['Lista de permisos':data.hashMApPermissions])
            def isDataExist = Utils.dataRequired(validDataExist, "Gestion de cuenta de empleado" , logId)
            if (isDataExist.status != 200) return respond(isDataExist.data, status:isDataExist.status)
        }
        def isValidData = validFormatData("Gestion de cuenta de empleado", data, logId)
        if (isValidData.status != 200) return respond(isValidData.data, status: isValidData.status)
        def deleteEmployeeResponse = EmployeesService.accountManagement(params, data, logId)
        return respond(deleteEmployeeResponse.data, status:deleteEmployeeResponse.status)
    }

    def statusEmployee(){
        def data = request.JSON
        def logId = new Logs("Gestion de cuenta de empleado","Inicio de solicitud", request, request.getHeader(Constants.HEADER_LOG_ID)).getId()
        Utils.logger(logId,"Gestion de cuenta de empleado", "Inicio de solicitud")
        if(!Utils.validateAccessProject( request.getHeader(Constants.HEADER_ORD_SERVICE))) return respond(TypeError.noPermissions(logId))
        if(data.date.isDate()) {
            new Logs( process, "El dato nombre de empleado no coincide el formato esperado que se quiere ingresar.", logId, "ERROR", false, [  data: data.name ] )
            Utils.logger(logId,process, "No coincide el formato esperado que se quiere ingresar.", data.name)
            def formatDate = TypeError.incorrectFormat( "Nombre de empleado", "valor alfanúmerico", logId )
            return respond(formatDate.data, status:formatDate.status)
        }
        def deleteEmployeeResponse = EmployeesService.accountManagement(params, data, logId)
        return respond(deleteEmployeeResponse.data, status:deleteEmployeeResponse.status)
    }
    
    def validFormatData(process, data, logId) {
        if (data.name && !data.name.isSpecialCharacters()) {
            new Logs( process, "El dato nombre de empleado no coincide el formato esperado que se quiere ingresar.", logId, "ERROR", false, [  data: data.name ] )
            Utils.logger(logId,process, "No coincide el formato esperado que se quiere ingresar.", data.name)
            return TypeError.incorrectFormat( "Nombre de empleado", "valor alfanúmerico", logId )
        }
        if (data.curp && !data.curp.isSpecialCharacters()) {
            new Logs( process, "El dato Curp de empleado no coincide el formato esperado que se quiere ingresar.", logId, "ERROR", false, [  data: data.curp ] )
            Utils.logger(logId,process, "No coincide el formato esperado que se quiere ingresar.", data.curp)
            return TypeError.incorrectFormat( "Curp de empleado", "valor alfanúmerico", logId )
        }
        if (data.rfc && !data.rfc.isSpecialCharacters()) {
            new Logs( process, "El dato RFC de empleado no coincide el formato esperado que se quiere ingresar.", logId, "ERROR", false, [  data: data.rfc ] )
            Utils.logger(logId,process, "No coincide el formato esperado que se quiere ingresar.", data.rfc)
            return TypeError.incorrectFormat( "RFC de empleado", "valor alfanúmerico", logId )
        }
        if (data.nss && !data.nss.isNss()) {
            new Logs( process, "El dato NSS de empleado no coincide el formato esperado que se quiere ingresar.", logId, "ERROR", false, [  data: data.nss ] )
            Utils.logger(logId,process, "No coincide el formato esperado que se quiere ingresar.", data.nss)
            return TypeError.incorrectFormat( "NSS de empleado", "valor alfanúmerico", logId )
        }
        if (data.phone && !data.phone.isPhoneNumber()) {
            new Logs( process, "El dato phone de empleado no coincide el formato esperado que se quiere ingresar.", logId, "ERROR", false, [  data: data.phone ] )
            Utils.logger(logId,process, "No coincide el formato esperado que se quiere ingresar.", data.phone)
            return TypeError.incorrectFormat( "telefono del empleado", "valor alfanúmerico", logId )
        }
        if (data.username  && !data.username.isInstitutionalEmail()) {
            new Logs( process, "El nombre de usuario no coincide con el formato esperado", logId, "ERROR", false, [  data: data.username ] )
            Utils.logger(logId, process, "El nombre de usuario no coincide con el formato esperado", data.username)
            return TypeError.incorrectFormat( "nombre de usuario", "correo empresarial", logId )
        }
        if (data.personalEmail && !data.personalEmail.isPersonalEmail()){
            new Logs( process, "El correo personal no coincide con el formato esperado", logId, "ERROR", false, [  data: data.username ] )
            Utils.logger(logId, process, "El correo personal no coincide con el formato esperado", data.username)
            return TypeError.incorrectFormat( "correo personal", "correo personal", logId )
        }
        if (data.hashMApPermissions) {
            for (permission in data.hashMApPermissions){
                def isValidUuid = Utils.validFormatUuid(process,"uuid permiso", permission, logId)
                if (isValidUuid.status != 200) return isValidUuid
            }
        }
        new Logs( process, "Validar datos de empleado", logId, "INFO", true, [ data: data, uuid: logId ] )
        Utils.logger(logId,process, "Validar datos de empleado", "Los datos cumplen con los valores esperados")
        return [data: [success: true], status:200]
    }
}
