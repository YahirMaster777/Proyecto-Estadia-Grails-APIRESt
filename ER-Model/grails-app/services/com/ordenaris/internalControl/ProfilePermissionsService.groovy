package com.ordenaris.internalControl

import grails.gorm.transactions.Transactional

@Transactional
class ProfilePermissionsService {

    def createPermissions(data, logId) {
        TemplatePermissions.withTransaction{status ->
            try{
                new Logs("Registrar Permisos Perfil", "Procesando Solicitud", logId, "INFO", true, [ : ])
                Utils.logger(logId,"Registrar Permisos Perfil","Procesando Solicitud")
                def template = Templates.findByName(data.template)
                if(!template){
                    new Logs( "Registrar Permisos Perfil", "No se encontró el registro perfil", logId, "ERROR", false, [  :  ] )
                    Utils.logger(logId, "Registrar Permisos Perfil", "No se encontró el registro del perfil")
                    return TypeError.informationNotFound(logId)
                }
                
                def permission = Permissions.findByAlias(data.permission)
                if(!permission){
                    new Logs( "Registrar Permisos Perfil", "No se encontró el registro del permiso", logId, "ERROR", false, [  :  ] )
                    Utils.logger(logId, "Registrar Permisos Perfil", "No se encontró el registro del permiso")
                    return TypeError.informationNotFound(logId)
                }
                
                def permisoExist = TemplatePermissions.findByPermissionAndTemplate( permission,template)
                if(permisoExist){
                    new Logs( "Registrar Permisos Perfil", "El perfil ya tiene el pemiso", logId, "ERROR", false, [  :  ] )
                    Utils.logger(logId, "Registrar Permisos Perfil", "El perfil ya tiene el pemiso")
                    return TypeError.existingRegister(logId)
                }
                
                
                def permissions = new TemplatePermissions()
                permissions.template = template
                permissions.uuidTemplate = template.uuid
                permissions.description = data.description
                permissions.permission = permission
                
                permissions.save(flush:true, failOnError:true)
                new Logs("Registrar Permisos Perfil", "Se registro el permiso", logId,"INFO", true,[ : ])
                Utils.logger(logId, "Registrar Permisos Perfil", "Se registro el permiso")
                return[data:[success:true], status:201]
                    
            }catch(e){
                new Logs("Registrar Permisos Perfil","Error en la solicitud", logId, e, [data:[success:false]])
                Utils.logger(logId, "Registrar Permisos Perfil", "Error en la solicitud", "ERROR: ${e.getMessage()}")
                status.setRollbackOnly()
                return TypeError.internalError(logId)
            }
        }
    }
}