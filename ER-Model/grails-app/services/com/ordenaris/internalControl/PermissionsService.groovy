package com.ordenaris.internalControl

import grails.gorm.transactions.Transactional

@Transactional
class PermissionsService {
    
    def createPermission(data, logId){
        Permissions.withTransaction{ status ->
            try{
                new Logs("Registrar Permiso", "Procesando solicitud",logId, "INFO", true, [ : ])
                Utils.logger(logId, "Registrar Permiso", "Procesando solicitud")
                
                
                
                def permissionExists = Permissions.findByAlias(data.alias)
                if(!permissionExists){
                    new Logs(process, "Ya existe un registro con esos datos", logId, "INFO", false, [ : ])
                    Utils.logger(logId, process, "Ya existe un registro con esos datos")
                    return TypeError.existingRegister(logId)
                }
                
                def section = Sections.findByName(data.section)
                if(!section){
                    new Logs("Registrar Permiso", "Seccion no encontrada", logId, "INFO", false, [ : ])
                    Utils.logger(logId ,"Registrar Permiso")
                    return TypeError.informationNotFound()
                }
                
                
                
                def permission = new Permissions()
                permission.section = section
                permission.name = data.name
                permission.alias = data.alias
                permission.description = data.description
                permission.save(flush:true, failOnError:true)
                
                new Logs("Registrar Permiso", "Se registro el permiso", logId, "INFO", true, [ : ])
                Utils.logger(logId, "Registrar Permiso", "Se registro el permiso", "Permiso: ${data.name}")
                return [data:[succes:true],status:200]
                
            }catch(e){
                new Logs("Registrar Permiso", "Error en la peticion", logId, e, [ : ])
                Utils.logger(logId, "Registrar Permiso", "Error en la peticion", "ERROR: ${e.getMessage()}")
                status.setRollbackOnly()
                return TypeError.internalError(logId)
            }
        
        }
        
    }
}
