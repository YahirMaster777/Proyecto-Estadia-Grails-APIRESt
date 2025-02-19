package com.ordenaris.internalControl


import grails.rest.*
import grails.converters.*

class EmployeesController {
	static responseFormats = ['json', 'xml']
	def EmployeesService
    def save(){
        def logId = new Logs("Registrar Empleado","Inicio de solicitud", request).getId()
        def data = request.JSON   
        Utils.logger(logId,"Registrar Empleado","Inicio de solicitud")
        
        def employee = Employees.findByPhoneAndPersonalEmail(data.phone, data.personalEmail).collect{ employee ->
                return [
                    personalEmail: employee.personalEmail,
                    phone: employee.phone,
                    name: employee.name
                ]
        }
        print(employee)
        
        Employees pe = Employees.find { status == "Activo" && status == "Inactivo" &&  personalEmail == data.personalEmail || phone == data.phone }
            if((employee.personalEmail == data.personalEmail) && (employee.phone == data.phone)){
                new Logs("Registrar Empleado","Se encontro a un empleado con los mismos datos", logId, "INFO", false,[data:employee] )
                Utils.logger(logId, "Registrar Empleado","Se encontro a un empleado con los mismos datos", "${data}")
                def saveEmployeeResponse = TypeError.existingRegister(logId)
                return respond(saveEmployeeResponse.data, status: saveEmployeeResponse.status) 
            }
        def saveEmployeeResponse = EmployeesService.createEmployee(data,logId)
        return respond(saveEmployeeResponse.data, status:saveEmployeeResponse.status)
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
    
    
    
}
