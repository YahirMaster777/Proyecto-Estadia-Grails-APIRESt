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
    
    def activateProfile(params, logId){
        Templates.withTransaction { status ->
            try{
                new Logs("Activar Perfil", "Procesando solicitud", logId, "INFO", true , [data:[params.uuid]])
                Utils.logger(logId,"Activar Perfil", "Procesando solicitud")
                def profile = Templates.findByUuid(params.uuid)
                
                if(!profile){
                    new Logs("Activar Perfil", "No se encontro la informacion solicitda",logId, "INFO",false,  [data:[para.uuid]])
                    Utils.logger(logId,"Activar Perfil", "No se encontro la informacion solicitada")
                    return TypeError.informationNotFound(logId)
                }
                profile.status ="Activo"
                profile.save(failOnError:true, flush:true)
                new Logs("Activar Perfil","Se activo el perfil", logId, "INFO", true, [data:[profile.name]])
                Utils.logger(logId,"Activar Perfil", "Se activo el perfil", "Perfil:${profile.name}")
                return [data:[success:true], status:200]
            }catch(e){
                new Logs("Activar Perfil","Error en la solicitud", logId, e, [ : ])
                Utils.logger(logId, "Activar Perfil", "Error en la solicitud", "ERROR: ${e.getMessage()}")
                status.setRollbackOnly()
                return TypeError.internalError(logId)
            }
        }
    }
    
    
    def deactivateProfile(params, logId){
        Templates.withTransaction{status ->
            try{
                new Logs("Desactivar Perfil", "Procesando solicitud",logId, "INFO",true, [data:params.uuid])
                Utils.logger(logId, "Desactivar Perfil", "Procesando solicitud")
                def profile = Templates.findByUuid(params.uuid){
                    new Logs("Desactivar Perfil", "No se encontro la informacion solicitada", logId, "INFO", false, [data:[params.uuid]])
                    Utils.logger(logId, "Desactivar Perfil", "Nose encontro la informacion solicitada")
                }
                
                profile.status = "Inactivo"
                profile.save(failOnError:true, flush:true)
                new Logs("Desactivar Perfil", "Se desactivo el perfil",logId, "INFO", true, [data:[profile.name]])
                Utils.logger(logId,"Desactivar Perfil","Se desactivo el perfil", "Perfil:${profile.name}")
                return [data:[success:true],status:200]
            }catch(e){
                new Logs("Desactivar Perfil","Error en la solicitud", logId, e, [ : ])
                Utils.logger(logId, "Desactivar Perfil", "Error en la solicitud", "ERROR: ${e.getMessage()}")
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
    def infoProfile(params, logId) {
        Templates.withTransaction{ status ->
            try {
                new Logs("Información del Perfil", "Procesando solicitud", logId, "INFO", true, [data: params.uuid])
                Utils.logger(logId, "Información del Perfil", "Procesando solicitud")
        
                def profile = Templates.findByUuid(params.uuid)
                if (!profile) {
                    new Logs("Información del Perfil", "No se encontró la información solicitada", logId, "INFO", false, [:])
                    Utils.logger(logId, "Información del Perfil", "No se encontró la información solicitada")
                    return TypeError.informationNotFound(logId)
                }
        
                def seccionesAgrupadas = [:]
                
                TemplatePermissions.findAllByTemplate(profile).each { templatePermission ->
                    def permiso = templatePermission.permission 
                    def seccion = permiso?.section  
        
                    if (seccion && permiso) {
                        if (!seccionesAgrupadas.containsKey(seccion.name)) {
                            seccionesAgrupadas[seccion.name] = [:]
                        }
                        seccionesAgrupadas[seccion.name][permiso.name] = permiso.alias
                    }
                }
        
                def secciones = seccionesAgrupadas.collect { nombreSeccion, permisos ->
                    return [
                        seccion  : nombreSeccion,
                        permisos : permisos
                    ]
                }
        
                def response = [
                    uuid       : profile.uuid,
                    name       : profile.name,
                    description: profile.description,
                    secciones  : secciones
                ]
        
                new Logs("Información del Perfil", "Perfil encontrado", logId, "INFO", true, [data: profile.name])
                Utils.logger(logId, "Información del Perfil", "Perfil encontrado", "Perfil: ${profile.name}")
        
                return [data: [success: true, data: response], status: 200]
        
            } catch (Exception e) {
                new Logs("Información del Perfil", "Error en la solicitud", logId, "ERROR", false, [:])
                Utils.logger(logId, "Información del Perfil", "Error en la solicitud: ${e.getMessage()}")
                status.setRollbackOnly()
                return TypeError.internalError(logId)
            }
        }
    }

    def allProfiles(logId){
        Templates.withTransaction{status ->
            try{
                new Logs("Lista de Perfiles", "Procesando solicitud", logId, "INFO", true, [ : ])
                Utils.logger(logId,"Lista de Perfiles", "Procesando solicitud")
                def profiles = Templates.getAll().collect(){ profile ->
                    return[
                        nombre: profile.name,
                        status: profile.status,
                        uuid: profile.uuid
                    ]
                }
                
                def countProfiles = Templates.count()
                new Logs("Lista de Perfiles", "Lista recuperada", logId, "INFO", true, [data:countProfiles])
                Utils.logger(logId, "Lista de Perfiles", "Lista recuperada", "${countProfiles}")
                return [data:[total:countProfiles, perfiles:profiles], status:200]
            }catch(e){
                new Logs("Lista de Perfiles", "Error en la solicitud", logId, "ERROR", false, [:])
                Utils.logger(logId, "Lista de Perfiles", "Error en la solicitud: ${e.getMessage()}")
                status.setRollbackOnly()
                return TypeError.internalError(logId)
            }
        }
    }

}
