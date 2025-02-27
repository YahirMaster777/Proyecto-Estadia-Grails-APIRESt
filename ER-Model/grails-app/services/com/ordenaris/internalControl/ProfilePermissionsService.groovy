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
                    Utils.logger(logId, "Registrar Permisos Perfil", "No se encontró el registro del perfil", "Position:${data.id}")
                    return TypeError.informationNotFound()
                }
                def section = Sections.findByName(data.section)
                if(!section){
                    new Logs( "Registrar Permisos Perfil", "No se encontró el registro de la seccion", logId, "ERROR", false, [  :  ] )
                    Utils.logger(logId, "Registrar Permisos Perfil", "No se encontró el registro de la seccion", "Position:${data.id}")
                    return TypeError.informationNotFound()
                }
                def permission = Permissions.findByAlias(data.permission)
                if(!permission){
                    new Logs( "Registrar Permisos Perfil", "No se encontró el registro del permiso", logId, "ERROR", false, [  :  ] )
                    Utils.logger(logId, "Registrar Permisos Perfil", "No se encontró el registro del permiso", "Position:${data.id}")
                    return TypeError.informationNotFound()
                }
                
                def permissions = new TemplatePermissions()
                permissions.template = template
                permissions.uuidTemplate = template.uuid
                permissions.section = section
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