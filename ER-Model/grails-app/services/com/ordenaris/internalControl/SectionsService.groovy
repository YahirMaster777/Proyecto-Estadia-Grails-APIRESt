package com.ordenaris.internalControl

import grails.gorm.transactions.Transactional

@Transactional
class SectionsService {

    def createSection(data, logId){
        Sections.withTransaction{ status ->
            try{
                new Logs("Registrar Seccion", "Procesando solicitud", logId, "INFO", true, [ : ])
                Utils.logger(logId, "Registrar Seccion", "Procesando solicitud")
                def sectionExists = Sections.findByName(data.name)
                if(sectionExists){
                    new Logs("Registrar Seccion","Ingrese otros Valores", logId,"INFO", false, [ : ])
                    Utils.logger(logId, "Registrar Seccion", "Ingrese otros valores")
                    return TypeError.existingRegister(logId)
                }
                def section = new Sections()
                section.name = data.name
                section.url = data.url
                section.description = data.description
                section.save(failOnError:true, flush:true)
                new Logs("Registrar Seccion", "Se registro la seccion", logId, "INFO", true, [data:data.name])
                Utils.logger(logId,"Registrar Seccion","Se registro la seccion", "${data.name}")
                return [data:[success:true], status:200]    
            }catch(e){
                new Logs("Registrar Seccion","Error en la solicitud",logId,e, [ : ])
                Utils.logger(logId,"Registrar Seccion","Error en la solicitud","ERROR: ${e.getMessage()}")
                status.setRollbackOnly()
                return TypeError.internalError(logId)
            }
    
        }
    }
    
    def updateSection(params, data, logId){
        Sections.withTransaction{ status -> 
            try{
                new Logs("Actualizar Seccion", "Procesando solicitud", logId,"INFO", true, [ : ])
                Utils.logger(logId,"Actualizar Seccion", "Procesando solicitud")
                
                def section = Sections.findByUuid(params.uuid)
                if(!section){
                    new Logs("Actualizar Seccion", "No se encontro la informacion", logId, "INFO", false, [ : ])
                    Utils.logger(logId, "Actualizar Seccion", "No se encontro la informacion")
                    return TypeError.informationNotFound(logId)
                }
                
                def sectionExists = Sections.findByName(data.name)
                if(sectionExists){
                    new Logs("Actualizar Seccion", "Ingrese otros Valores", logId, "INFO", false, [ : ])
                    Utils.logger(logId, "Actualizar Seccion", "Ingrese otros Valores")
                    return TypeError.existingRegister(logId)
                }
                
                data.name?section.name = data.name:section.name
                data.url?section.url = data.url:section.url
                data.status?section.status= data.status:section.status
                data.description?section.description = data.description:section.description
                section.save(failOnError:true, flush:true)
                
                new Logs("Actualizar Seccion", "Se actualizo la seccion",logId, "INFO", true, [ data: params.uuid])
                Utils.logger(logId, "Actualizar Seccion", "Se actualizo la seccion", "Seccion: ${params.uuid}")
                return [data:[success:true], status:200]
                
            }catch(e){
                new Logs("Actualizar Seccion", "Error en la solicitud", logId, e , [ : ])
                Utils.logger(logId, "Actualizar Seccion","Error en la solicitud", "ERROR:${e.getMessage()}")
                return TypeError.informationNotFound(logId)
            
            }
        }
        
    }
    
    def changeStatus(params, logId){
        Sections.withTransaction{
            try{
                new Logs("Cambiar status seccion", "Procesando solicitud", logId, "INFO", true, [ : ])
                Utils.logger(logId, "Cambiar status seccion", "Procesando solicitud")
                def section = Sections.findByUuidAndStatus(params.uuid, !"Deprecada")
                if(!section){
                    new Logs("Cambiar status seccion", "No se encontro la informacion solicitada", logId, "INFO", false, [ : ])
                    Utils.logger(logId, "Cambiar status seccion", "No se encontro la informacion solicitada")
                    return TypeError.informationNotFound(logId)
                }
                
                if(params.actionService == 'activate'){
                    new Logs("Cambiar status seccion", "Activar seccion", logId,"INFO", true, [ : ])
                    Utils.logger(logId, "Cambiar status seccion", "Activar seccion")
                    section.status='Activa'
                    section.save(failOnError:true, flush:true)
                }
                
                if(params.actionService == 'deactivate'){
                    new Logs("Cambiar status seccion","Desactivar seccion", logId, "INFO", true, [ : ])
                    Utils.logger(logId, "Cambiar status seccion", "Desactivar seccion")
                    section.status='Inactiva'
                    section.save(failOnError:true, flush:true)
                }
                
                
                
                
                
            }catch(e){
                
            }
        }
    }
    
    def activateSection(params, logId){
       Sections.withTransaction{ status ->
            try{
                new Logs("Activar Seccion", "Procesando solicitud", logId, "INFO", true, [data:params.uuid])
                Utils.logger(logId, "Activar Seccion", "Procesando solictud", "Seccion :${params.uuid}")
                
                def section = Sections.findByUuid(params.uuid)
                if(!section){
                    new Logs("Activar Seccion","No se encontro la seccion", logId, "INFO",false, [data:params.uuid])
                    Utils.logger(logId, "Activar Seccion", "No se encontro la seccion", "Seccion :${params.uuid}")
                    return TypeError.informationNotFound(logId)
                }
                section.status = "Activa"
                section.save(flush:true, failOnError:true)
                
                new Logs("Activar Seccion", "Se activo la seccion", logId, "INFO", true, [data:params.uuid])
                Utils.logger(logId,"Activar Seccion", "Se activo la seccion", "Seccion :${params.uuid}")
                return [data:[success:true], status:200]
            }catch(e){
                new Logs("Activar Seccion", "Error en la solicitud", logId, e, [ : ])
                Utils.logger(logId,"Activar Seccion", "Error en la solicitud", "ERROR: ${e.getMessage()}")
                status.setRollbackOnly()
                return TypeError.internalError(logId)
            }
       }
        
    }
    
    def deactivateSection(params, logId){
        Sections.withTransaction{status ->
            try{
                new Logs("Desactivar Seccion", "Procesando solicitud", logId, "INFO", true, [data:params.uuid])
                Utils.logger(logId,"Desactivar Seccion", "Procesando solicitud", "Seccion :${params.uuid}")
                
                def section = Sections.findByUuid(params.uuid)
                if(!section){
                    new Logs("Desactivar Seccion","No se encontro la seccion", logId, "INFO",false, [data:params.uuid])
                    Utils.logger(logId, "Desactivar Seccion", "No se encontro la seccion", "Seccion :${params.uuid}")
                    return TypeError.informationNotFound(logId)
                }
                section.status ="Inactiva"
                section.save(failOnError:true, flush:true)
                
                new Logs("Desactivar Seccion", "Se desactivo la seccion",logId, "INFO",true, [data:params.uuid])
                Utils.logger(logId, "Desactivar Seccion", "Se desactivo la seccion", "Seccion :${params.uuid}")
                return [data:[success:true], status:200]
            }catch(e){
                new Logs("Desactivar Seccion", "Error en la solicitud", logId, e, [data:[success:false]])
                Utils.logger("Desactivar Seccion", "Error en la solicitud", "ERROR: ${e.getMessage()}") 
                status.setRollbackOnly()
                return TypeError.internalError(logId)
            }
        
        }
    }
    
}
