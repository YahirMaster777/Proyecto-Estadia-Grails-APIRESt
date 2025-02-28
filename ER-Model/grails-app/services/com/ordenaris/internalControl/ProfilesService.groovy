package com.ordenaris.internalControl

import grails.gorm.transactions.Transactional

@Transactional
class ProfilesService {

    def createProfile(data, logId){
        Templates.withTransaction{status ->
            try{
                new Logs("Registrar Perfil", "Procesando solicitud", logId,"INFO", true, [data:data.name])
                Utils.logger(logId,"Registrar Perfil","Procesando solicitud")

                def profile = Templates.findByName(data.name)
                if(profile){
                    new Logs("Registrar Perfil", "No es posible procesar la solicitud, por favor utilice valores diferentes.", logId,"INFO",false, [ : ])
                    Utils.logger(logId, "Registrar Perfil","No es posible procesar la solicitud, por favor utilice valores diferentes.")
                    return TypeError.existingRegister(logId)
                }

                def template = new Templates()
                template.name = data.name
                template.description = data.description
                template.save(failOnError:true, flush:true)
                new Logs("Registrar Perfil", "Se registro el perfil", logId, "INFO",true, [data:data.name])
                Utils.logger(logId, "Registrar Perfil", "Se registro el perfil", "Nombre:${data.name}")
                return [data:[success:true], status:200]
            }catch(e){
                new Logs("Registrar Perfil","Error en la solicitud", logId, e, [ : ])
                Utils.logger(logId, "Registrar Perfil", "Error en la solicitud", "ERROR: ${e.getMessage()}")
                status.setRollbackOnly()
                return TypeError.internalError(logId)
            }
        }
    }

    def updateProfile(params, data, logId){
        Templates.withTransaction{ status ->
            try{
                new Logs("Editar Perfil", "Procesando solicitud", logId, "INFO",true, [data:params.uuid])
                Utils.logger(logId, "Editar Perfil", "Procesando solicitud")
                def profile = Templates.findByUuid(params.uuid)
                if(!profile){
                    new Logs("Editar Perfil", "No se encontro la Informacion solicitada",logId, "INFO", false, [data:params.uuid])
                    Utils.logger(logId, "Editar Perfil", "No se encontro la Informacion solicitada")
                    return TypeError.informationNotFound(logId)
                }

                def profile2 = Templates.findByName(data.name)
                if(profile2){
                    new Logs("Editar Perfil", "No es posible procesar la solicitud, por favor utilice valores diferentes.", logId, "INFO", false, [ : ])
                    Utils.logger(logId, "Editar Perfil", "No es posible procesar la solicitud, por favor utilice valores diferentes.")
                    return TypeError.existingRegister(logId)
                }
                profile.properties = data
                // data.name?profile.name= data.name:profile.name
                // data.description?profile.description= data.description:profile.description
                profile.save(flush:true, failOnError:true)

                new Logs("Editar Perfil", "Se actualizo el perfil", logId, "INFO", true, [data:params.uuid])
                Utils.logger(logId,"Editar Perfil", "Se actualizo el perfil")
                return [data:[success:true], status:200]
            }catch(e){
                new Logs("Editar Perfil","Error en la solicitud", logId, e, [ : ])
                Utils.logger(logId, "Editar Perfil", "Error en la solicitud", "ERROR: ${e.getMessage()}")
                status.setRollbackOnly()
                return TypeError.internalError(logId)
            }
        }
    }

    def deleteProfile(params, logId){
        Templates.withTransaction{status ->
            try{
                new Logs("Eliminar Perfil", "Procesando solicitud", logId, "INFO", true, [data:params.uuid])
                Utils.logger(logId,"Eliminar Perfil", "Procesanndo solicitud")

                def profile = Templates.findByUuid(params.uuid)
                if(!profile){
                    new Logs("Eliminar Perfil", "No se encontro la Informacion solicitada", logId,"INFO", false, [ : ])
                    Utils.logger(logId, "Eliminar Perfil", "No se encontro la Informacion solicitada")
                    return TypeError.informationNotFound(logId)
                }

                profile.delete(failOnError:true, flush:true)
                new Logs("Eliminar Perfil", "Se elimino el Perfil", logId, "INFO", true, [data:params.uuid])
                Utils.logger(logId, "Eliminar Perfil", "Se elimino el perfil")
                return [data:[success:true], status:200]

            }catch(e){
                new Logs("Eliminar Perfil","Error en la solicitud", logId, e, [ : ])
                Utils.logger(logId, "Eliminar Perfil", "Error en la solicitud", "ERROR: ${e.getMessage()}")
                status.setRollbackOnly()
                return TypeError.internalError(logId)
            }
        }
    }
    
    def infoProfile(params, logId){
        try{
            new Logs("Informacion del perfil", "Procesando solicitud",logId, "INFO", true, [data:params.uuid])
            Utils.logger(logId,"Informacion del perfil", "Procesando solicitud")
            def permissions = TemplatePermissions.findAllByUuidTemplate(params.uuid).collect { permissions ->
                return [  
                    permiso: permissions.permissionId
                ]
            }
            
            // def permiso = Permissions.findByUuid(permissions.permissionId){permiso ->
            //     return[
            //         uuidSeccion: permiso.uuidSection
            //     ]
            
            // }
            
            def secciones = Sections.findByUuid(){ seccion ->
                return[
                    nombre: seccion.name,
                    permisos: permissions
                ]
                
            }
            
            def section = TemplatePermissions.findAllByUuidTemplateAnd(params.uuid).collect{ section ->
                return [
                    seccion :section.sectionId,
                    permisos: permissions
                ]
                
            }
            
            
          
            def profile = Templates.findByUuid(params.uuid).collect { profile ->
                return [
                    uuid       : profile.uuid,
                    name       : profile.name,
                    description: profile.description,
                    secciones: section
                ]
            }
            
            if (!profile) {
                new Logs("Información del perfil", "No se encontró la información solicitada", logId, "INFO", false, [:])
                Utils.logger(logId, "Información del perfil", "No se encontró la información solicitada")
                return TypeError.informationNotFound(logId)
            }
            

            new Logs("Informacion del perfil", "Perfil Encontrado", logId, "INFO", true, [data:params.uuid])
            Utils.logger(logId,"Informacion del perfil", "Perfil Encontrado")
            return [data:[success:true,data:profile], status:200]
            
            
        }catch(e){
            new Logs("Informacion del perfil","Error en la solicitud", logId, e, [ : ])
            Utils.logger(logId, "Informacion del perfil", "Error en la solicitud", "ERROR: ${e.getMessage()}")
            return TypeError.internalError(logId)
        }
    }


}
