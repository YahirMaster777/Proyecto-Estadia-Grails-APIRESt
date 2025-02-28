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
                new Logs("Registrar Seccion","Error en la solicitud",logId,e, [data:[success:false]])
                Utils.logger(logId,"Registrar Seccion","Error en la solicitud","ERROR: ${e.getMessage()}")
                status.setRollbackOnly()
                return TypeError.internalError(logId)
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
                new Logs("Activar Seccion", "Error en la solicitud", logId, e, [data:[success:false]])
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
