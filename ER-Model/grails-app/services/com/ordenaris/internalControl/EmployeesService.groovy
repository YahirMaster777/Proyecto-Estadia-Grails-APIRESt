package com.ordenaris.internalControl

import grails.gorm.transactions.Transactional

@Transactional
class EmployeesService {

    def createEmployee(data, logId) {
        Employees.withTransaction{status ->
            try{
                new Logs("Registrar Empleado", "Procesando Solicitud", logId, "INFO", true, [data:data.name])
                Utils.logger(logId,"Registrar Empleado","Procesando Solicitud")
                def position = PositionEmployees.findById(data.position)
                if (!position) {
                    new Logs( "Registrar Empleado", "No se encontró el registro", logId, "ERROR", false, [ uuidEmployee:data.id ] )
                    Utils.logger(logId, "Registrar Empleado", "No se encontró el registro", "Position:${data.id}")
                    return TypeError.informationNotFound( logId )
                }
                def company = Enterprises.findById(data.company)
                if (!company) {
                    new Logs( "Registrar Empleado", "No se encontró el registro", logId, "ERROR", false, [ companyUuid:data.id ] )
                    Utils.logger(logId, "Registrar Empleado", "No se encontró el registro", "Compañia:${data.id}")
                    return TypeError.informationNotFound( logId )
                }
                def employee = new Employees()
                employee.curp = data.curp
                employee.phone = data.phone
                employee.idEmployee = data.idEmployee
                employee.rfc = data.rfc
                employee.dismissedDate = data.dismissedDate
                employee.lastName1 = data.lastName1
                employee.lastName2 = data.lastName2
                employee.name = data.name
                employee.nss = data.nss
                employee.manage = data.manage
                employee.position = position
                employee.company = company    
                employee.personalEmail = data.personalEmail
                employee.initialDate = data.initialDate
                data.status?employee.status= data.status:employee.status
                employee.save(flush:true, failOnError:true)
                new Logs("Registrar Empleado", "Se registro el empleado", logId,"INFO", true,[data:data.name])
                Utils.logger(logId, "Registrar Empleado", "Se registro el empleado", "Nombre:${data.name}")
                return[data:[success:true], status:201]
            }catch(e){
                new Logs("Registrar Empleado","Error en la solicitud", logId, e, [data:[success:false]])
                Utils.logger(logId, "Registrar Empleado", "Error en la solicitud", "ERROR: ${e.getMessage()}")
                status.setRollbackOnly()
                return TypeError.internalError(logId)
            }
        }
    }
    
    def updateEmployee(params, data, logId){
        Employees.withTransaction{ status -> 
            try{
                new Logs("Editar Empleado", "Procesando Solicitud",logId,"INFO", true, [data:params.uuid])
                Utils.logger(logId,"Editar Empleado","Procesando Solicitud")
                def employee = Employees.findByUuidAndStatus(params.uuid, "Activo")
                if (!employee){
                    new Logs("Editar Empleado","No se encontro el empleado(a)", logId, "INFO", false,[data:params.uuid] )
                    Utils.logger(logId, "Editar Empleado","No se encontro el empleado(a)")
                    return TypeError.informationNotFound(logId)
                }
                employee.properties = data
                employee.save(flush:true, failOnError:true)
                new Logs("Editar Empleado", "Informacion Actualizada", logId, "INFO", true, [data:[params.uuid]])
                Utils.logger(logId,"Editar Empleado","Informacion Actualizada", "UUID:${params.uuid}")
                return [data:[success:true], status:200]
            }catch(e){
                new Logs("Editar Empleado","Error en la solicitud", logId, e, [data:[success:false]])
                Utils.logger(logId, "Editar Empleado", "Error en la solicitud", "ERROR: ${e.getMessage()}")
                status.setRollbackOnly()
                return TypeError.internalError(logId)
            }
        }
    }
    
    def deleteEmployee(params, logId){
        Employees.withTransaction{ status ->
            try{
                new Logs("Eliminar Empleado","Procesando Solicitud", logId,"INFO", true, [data:params.uuid])
                Utils.logger(logId,"Eliminar Empleado", "Procesando Solicitud")
                def employee = Employees.findByUuidAndStatus(params.uuid,"Activo")
                if(!employee){
                    new Logs("Eliminar Empleado", "No se encontro el empleado(a)", logId, "INFO", false, [data:params.uuid])
                    Utils.logger(logId,"Eliminar Empleado","No se encontro el empleado(a)")
                    return TypeError.informationNotFound(logId)
                }
                employee.status="Inactivo"
                employee.save(flush:true, failOnError:true)
                new Logs("Eliminar Empleado","Se elimino el registro", logId, "INFO",true, [data:params.uuid])
                Utils.logger(logId,"Eliminar Empleado","Se elimino el registro","UUID:${params.uuid}")
                return[data:[success:true],status:200]
            }catch(e){
                new Logs("Eliminar Empleado","Error en la solicitud", logId, e, [data:[success:false]])
                Utils.logger(logId, "Eliminar Empleado", "Error en la solicitud", "ERROR: ${e.getMessage()}")
                status.setRollbackOnly()
                return TypeError.internalError(logId)
            }
        } 
    }
}