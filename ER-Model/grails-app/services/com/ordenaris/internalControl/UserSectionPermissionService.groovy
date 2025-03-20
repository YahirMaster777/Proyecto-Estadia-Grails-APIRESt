package com.ordenaris.internalControl

import grails.gorm.transactions.Transactional

@Transactional
class UserSectionPermissionService {

    def createUserPermission(data, logId) {
        try{
            new Logs("Asignar permisos al usuario", "Procesando Solicitud", logId, "INFO", true, [uuidUserdata.uuidUser])
            Utils.logger(logId,"Asignar permisos al usuario","Procesando Solicitud")
            def user = Users.findByUuid(data.uuidUser)
            if(!user){
                new Logs("Asignar permisos al usuario", "No se encontro el empleado(a)", logId, "INFO", false, [uuidUserdata.uuidUser])
                Utils.logger(logId,"Asignar permisos al usuario","No se encontro el empleado(a)")
                return TypeError.informationNotFound(logId)
            }
            data.hashMApPermissions.each { permiss ->
                def permission = Permissions.findByUuid(permiss)
                if(!permission){
                    new Logs("Asignar permisos al usuario","No se encontro la informacion solicitada", logId, "INFO", false,[uuidPermission:permiss] )
                    Utils.logger(logId, "Asignar permisos al usuario","No se encontro la informacion solicitada")
                    return TypeError.informationNotFound(logId)
                }
                try {
                    def userPermission = new UserSectionPermission()
                    userPermission.user = user
                    userPermission.permission = permission
                    userPermission.save(flush:true, failOnError:true)
                } catch (Exception e) {
                    new Logs("Asignar permisos al usuario","Error en la solicitud", logId, e, [data:[success:false]])
                    Utils.logger(logId, "Asignar permisos al usuario", "Error en la solicitud", "f: ${e.getMessage()}")
                    return TypeError.internalError(logId)
                }
            }
            return[data:[success:true],status:200]
        }catch(Exception e) {
            uspStatus.setRollbackOnly()
            new Logs("Asignar permisos al usuario","Error en la solicitud", logId, e, [data:[success:false]])
            Utils.logger(logId, "Asignar permisos al usuario", "Error en la solicitud", "f: ${e.getMessage()}")
            return TypeError.internalError(logId)
        }
    }
}
