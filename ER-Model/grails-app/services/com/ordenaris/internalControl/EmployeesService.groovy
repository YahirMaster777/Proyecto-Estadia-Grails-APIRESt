package com.ordenaris.internalControl

import grails.gorm.transactions.Transactional

@Transactional
class EmployeesService {
    def UserSectionPermissionService
    def RecoveryService

    def createEmployee(data, account = null, activation = null, logId) {
        Employees.withTransaction{eStatus->
            try{
                new Logs("Registrar Empleado", "Procesando Solicitud", logId, "INFO", true, [data:data.name])
                Utils.logger(logId,"Registrar Empleado","Procesando Solicitud")
                def position = PositionEmployees.findByUuid(data.uuidPosition)
                if (data.uuidPosition && !position) {
                    new Logs( "Registrar Empleado", "No se encontró el registro", logId, "ERROR", false, [ uuidPosition:data.uuidPosition ] )
                    Utils.logger(logId, "Registrar Empleado", "No se encontró el registro", "Position:${data.uuidPosition}")
                    return TypeError.informationNotFound( logId )
                }
                def company = Enterprises.findByUuid(data.uuidCompany)
                if (data.uuidCompany && !company) {
                    new Logs( "Registrar Empleado", "No se encontró el registro", logId, "ERROR", false, [ uuidCompany:data.uuidCompany ] )
                    Utils.logger(logId, "Registrar Empleado", "No se encontró el registro", "Compañia:${data.uuidCompany}")
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
                data.status?employee.status = data.status:employee.status
                def user = Users.findByUsername(data.username)
                if (user) {
                    new Logs("Registrar Empleado", "Datos existentes, por favor utilice valores diferentes", logId, "INFO", false, [data: [success: false]])
                    Utils.logger(logId, "Registrar Empleado", "Datos existentes, por favor utilice valores diferentes", data.username )
                    return TypeError.existingRegister(logId)
                }
                println "el account " + account
                if (account) {
                    // TODO: mandar a llamar accountManagement
                    user= new Users(username: data.username, employee:employee)
                    println "el usuario " + user + " con su uuid " + user.uuid
                    UserSectionPermissionService.createUserPermission([uuidUser:employee.uuid, hashMApPermissions:data.hashMApPermissions], logId)
                }
                if (account && activation) {
                    RecoveryService.createToken(user.username, logId, false)
                }
                employee.save(flush:true, failOnf:true)
                user?.save(flush:true, failOnf:true)
                new Logs("Registrar Empleado", "Se registro el empleado", logId,"INFO", true,[uuidEmployee: employee.uuid])
                Utils.logger(logId, "Registrar Empleado", "Se registro el empleado", employee.uuid)
                return[data:[success:true], status:200]
            }catch(e){
                eStatus.setRollbackOnly()
                new Logs("Registrar Empleado","Error en la solicitud", logId, e, [data:[success:false]])
                Utils.logger(logId, "Registrar Empleado", "Error en la solicitud", "f: ${e.getMessage()}")
                return TypeError.internalError(logId)
            }
        }
    }
    
    def updateEmployee(params, data, logId){
        Employees.withTransaction{ eStatus-> 
            try{
                new Logs("Editar Empleado", "Procesando Solicitud",logId,"INFO", true, [data:params.uuid])
                Utils.logger(logId,"Editar Empleado","Procesando Solicitud")
                def employee = Employees.findByUuidAndStatus(params.uuid, "activo")
                if (!employee){
                    new Logs("Editar Empleado","No se encontro la informacion solicitada", logId, "INFO", false,[data:params.uuid] )
                    Utils.logger(logId, "Editar Empleado","No se encontro la informacion solicitada")
                    return TypeError.informationNotFound(logId)
                }
                employee.properties = data
                employee.save(flush:true, failOnf:true)
                new Logs("Editar Empleado", "Informacion Actualizada", logId, "INFO", true, [data:[params.uuid]])
                Utils.logger(logId,"Editar Empleado","Informacion Actualizada", "UUID:${params.uuid}")
                return [data:[success:true], status:200]
            }catch(e){
                eStatus.setRollbackOnly()
                new Logs("Editar Empleado","Error en la solicitud", logId, e, [data:[success:false]])
                Utils.logger(logId, "Editar Empleado", "Error en la solicitud", "f: ${e.getMessage()}")
                return TypeError.internalError(logId)
            }
        }
    }

    // TODO: gestion de ingreso/baja de empleado para la tabla de empleados

    def accountManagement(params, data, logId){
        // TODO: modificarlo para la tabla dev usuarios
        Employees.withTransaction{ eStatus->
            try{
                new Logs("Gestion de cuenta de empleado","Procesando Solicitud", logId,"INFO", true, [data:params.uuid])
                Utils.logger(logId,"Gestion de cuenta de empleado", "Procesando Solicitud")
                def employee = Employees.findByUuid(params.uuid)
                if(!employee) {
                    new Logs("Gestion de cuenta de empleado", "No se encontro la informacion solicitada", logId, "INFO", false, [data:params.uuid])
                    Utils.logger(logId,"Gestion de cuenta de empleado","No se encontro la informacion solicitada", params.uuid)
                    return TypeError.informationNotFound(logId)
                }
                if (params.actionService == employee.status) {
                    new Logs("Gestion de cuenta de empleado", "Ya cuenta con el status que se quiere ingresar", logId, "INFO", false, [data:params.uuid])
                    Utils.logger(logId,"Gestion de cuenta de empleado","Ya cuenta con el status que se quiere ingresar", params.uuid)
                    return TypeError.existingRegister(logId)
                }
                employee.status = params.actionService
                employee.save(flush:true, failOnError:true)
                def user = Users.findByEmployee(employee)
                if (params.activation && !user){
                    user = new Users(username: data.username, employee:employee )
                    user.save(flush:true, failOnf:true)
                    UserSectionPermissionService.createUserPermission([uuidUser:user.uuid, hashMApPermissions:data.hashMApPermissions], logId)
                    RecoveryService.createToken(user.username, logId, true)
                }
                new Logs("Gestion de cuenta de empleado","Se hac cambiado el estatus", logId,"INFO", true, [data:params.uuid])
                Utils.logger(logId,"Gestion de cuenta de empleado", "Se hac cambiado el estatus", params.actionService)
                return[data:[success:true],status:200]
            } catch (Exception e) {
                eStatus.setRollbackOnly()
                new Logs("Gestion de cuenta de empleado","Error en la solicitud", logId, e, [data:[success:false]])
                Utils.logger(logId, "Gestion de cuenta de empleado", "Error en la solicitud", "f: ${e.getMessage()}")
                return TypeError.internalError(logId)
            }
        }
    }
}