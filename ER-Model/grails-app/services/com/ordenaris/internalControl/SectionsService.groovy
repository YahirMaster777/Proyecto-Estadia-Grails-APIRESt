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
    
    def updateStatus(params, logId){
        Sections.withTransaction{ status ->
            try{
                new Logs("Actualizar status de seccion", "Procesando solicitud", logId, "INFO", true, [ : ])
                Utils.logger(logId, "Actualizar status de seccion", "Procesando solicitud")
                
                def section = Sections.findByUuid(params.uuid)
                println section.name
                if(!section){
                    new Logs("Actualizar status de seccion", "No se encontro la informacion solicitada", logId, "INFO", false, [ : ])
                    Utils.logger(logId, "Actualizar status de seccion", "No se encontro la informacion solicitada")
                    return TypeError.informationNotFound(logId)
                }
                def actions = ['activate', 'deactivate']
                if(actions.indexOf(params.actionService)< 0){
                    new Logs("Actualizar status de seccion", "Accion invalida", logId, "INFO", false, [ : ])
                    Utils.logger(logId, "Actualizar status de seccion", "Accion invalida")
                    return TypeError.invalidData(params.actionService, logId)
                }
                
                if(params.actionService == 'activate'){
                    new Logs("Actualizar status de seccion", "Activar seccion", logId,"INFO", true, [ : ])
                    Utils.logger(logId, "Actualizar status de seccion", "Activar seccion")
                    section.status='activa'
                    section.save(failOnError:true, flush:true)
                    new Logs("Actualizar status de seccion", "Se activo la seccion", logId, "INFO", true, [ : ])
                    Utils.logger(logId, "Actualizar status de seccion", "Se activo la seccion")
                }
                
                if(params.actionService == 'deactivate'){
                    new Logs("Actualizar status de seccion","Desactivar seccion", logId, "INFO", true, [ : ])
                    Utils.logger(logId, "Actualizar status de seccion", "Desactivar seccion")
                    section.status='Inactiva'
                    section.save(failOnError:true, flush:true)
                    new Logs("Actualizar status de seccion", "Se desactivo la seccion", logId, "INFO", true, [ : ])
                    Utils.logger(logId, "Actualizar status de seccion", "Se desactivo la seccion")
                }
                return [data:[succes:true],status:200]
                
            }catch(e){
                new Logs("Actualizar status de seccion", "Error en la solicitud", logId, e , [  : ])
                Utils.logger(logId, "Actualizar status de seccion", "Error en la solicitud", "ERROR: ${e.getMessage()}")
                status.setRollbackOnly()
                return TypeError.internalError(logId)
                
            }
        }
    }
    
    def deleteSection(params, logId){
        Sections.withTransaction{ status -> 
            try{
                new Logs("Eliminar seccion", "Procesando solicitud",logId, "INFO",true, [ : ])
                Utils.logger(logId, "Eliminar seccion", "Procesando solicitud")
                
                def section = Sections.findByUuid(params.uuid)
                if(!section){
                    new Logs("Eliminar seccion", "Procesando solicitud", logId, "INFO", true, [ : ])
                    Utils.logger(logId,"Eliminar seccion", "Procesando solicitud")
                    return TypeError.informationNotFound(logId)
                }
                
                section.uuid = "_delete_"+ new Date().log()
                section.status = 'Inactiva'
                section.save(flush:true, failOnError:true)
                new Logs("Eliminar seccion", "Se elimino la seccion", logId, "INFO", true, [ : ])
                Utils.logger(logId, "Eliminar seccion", "Se elimmino la seccion")
                return [data:[succes:true], status:200]    
            }catch(e){
                new Logs("Eliminar seccion", "Error en la solicitud", logId, e, [ : ])
                Utils.logger(logId, "Eliminar seccion", "Error en la solicitud", "ERROR: ${e.getMessage()}")
                status.setRollbackOnly()
                return TypeError.internalError(logId)
            }
        }
    }
    
    def allSections(logId){
        Sections.withTransaction{ status ->
            try{
                new Logs("Lista de secciones", "Procesando solicitud", logId, "INFO",true, [ : ])
                Utils.logger(logId, "Lista de secciones", "Procesando solicitud")
                def sections = Sections.getAll().collect(){ section ->
                    return[
                        nombre: section.name,
                        uuid  : section.uuid,
                        url   : section.url,
                        descripcion: section.description
                    ]
                }
                new Logs("Lista de secciones", "Lista recuperda", logId, "INFO", true, [ : ])
                Utils.logger(logId, "Lista de secciones","Lista recuperada")
                return [data:[succes:true,data:[secciones: sections]], status:200]
            }catch(e){
                new Logs("Lista de secciones", "Error en la solicitud", logId, e, [ : ])
                Utils.logger(logId, "Lista de secciones", "Error en la solicitud","ERROR: ${e.getMessage()}")
                status.setRollbackOnly()
                return TypeError.internalControl(logId)
            }
        }
    }


    
}
