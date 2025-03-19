package com.ordenaris.internalControl

import grails.gorm.transactions.Transactional

@Transactional
class UserSectionPermissionService {

    def createUserPermission(data, logId) {
        UserSectionPermission.withTransaction{uspStatus->
            try{
                println "imprimiendo la data " + data
                new Logs("Asignar permisos al Empleado", "Procesando Solicitud", logId, "INFO", true, [data:data.uuidUser])
                Utils.logger(logId,"Asignar permisos al Empleado","Procesando Solicitud")
                def user = Users.findByUuid(data.uuidUser)
                println "este es el usuario " + user
                if(!user){
                    new Logs("Asignar permisos al Empleado", "No se encontro el empleado(a)", logId, "INFO", false, [data:data.uuidUser])
                    Utils.logger(logId,"Asignar permisos al Empleado","No se encontro el empleado(a)")
                    return TypeError.informationNotFound(logId)
                }
                data.hashMApPermissions.each{permission ->
                    try {
                        println "guardando el usuario $user con el permiso $permission"
                        new UserSectionPermission(user:user, permission:permission)
                    } catch (Exception e) {
                        new Logs("Asignar permisos al Empleado","Error en la solicitud", logId, e, [data:[success:false]])
                        Utils.logger(logId, "Asignar permisos al Empleado", "Error en la solicitud", "ERROR: ${e.getMessage()}")
                        return TypeError.internalError(logId)
                    }
                }
            }catch(Exception e) {
                uspStatus.setRollbackOnly()
                new Logs("Asignar permisos al Empleado","Error en la solicitud", logId, e, [data:[success:false]])
                Utils.logger(logId, "Asignar permisos al Empleado", "Error en la solicitud", "ERROR: ${e.getMessage()}")
                return TypeError.internalError(logId)
            }
        }
    }
}
