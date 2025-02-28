package com.ordenaris.internalControl

import grails.gorm.transactions.Transactional

@Transactional
class AppsService {

    def createApp(data, logId){
        Apps.withTransaction{ status-> 
            try{
                new Logs("Registrar Aplicacion", "Procesando solicitud",logId, "INFO", true, [data:data.name])
                Utils.logger(logId, "Registrar Aplicacion", "Procesando solicitud")
                def aplication = new Apps()
                aplication.port = data.port
                aplication.criticality = data.criticality
                aplication.versionApp = data.versionApp
                aplication.dateUndeploy = data.dateUndeploy
                aplication.name = data.name
                aplication.type = data.type
                aplication.urlRepository = data.urlRepository
                aplication.domain= data.domain
                aplication.description = data.description
                data.status?aplication.status= data.status:aplication.status
                aplication.save(failOnError:true, flush:true)
                new Logs("Registrar Aplicacion","Se registro la aplicacion", logId, "INFO", true,[data:data.name])
                Utils.logger(logId, "Registrar Aplicacion","Se registro la aplicacion", "Nombre:${data.name}")
                return [data:[success:true],status:200]
            }catch(e){
                new Logs("Registrar Aplicacion","Error en la solicitud", logId, e, [data:[success:false]])
                Utils.logger(logId, "Registrar Aplicacion", "Error en la solicitud", "ERROR: ${e.getMessage()}")
                status.setRollbackOnly()
                return TypeError.internalError(logId)
            }
        } 
    }
    
    def activeApp(params, logId){
        Apps.withTransaction{ status ->
            try{
                new Logs("Activar Aplicacion", "Procesando solicitud", logId, "INFO", true, [data:params.uuid])
                Utils.logger(logId,"Activar Aplicacion", "Procesando solicitud", "${params.uuid}")
                def app = Apps.findByUuid(params.uuid)
                
                if(!app){
                    new Logs("Activar Aplicacion", "No se encontro la aplicacion", logId, "INFO", false, [data:params.uuid])
                    Utils.logger(logId, "Activar Aplicacion","No se encontro la aplicacion", "${params.uuid}")
                    return TypeError.informationNotFound(logId)
                }
                
                if(app.status == "Activa"){
                    new Logs("Activar Aplicacion", "Ya esta Activa", logId, "INFO", false, [data:params.uuid])
                    Utils.logger(logId, "Activar Aplicacion","Ya esta Activa", "${params.uuid}")
                    return TypeError.existingRegister(logId)
                }
                
                app.status="Activa"
                app.save(failOnError:true, flush:true)
                new Logs("Activar Aplicacion", "Se Activo la Aplicacion", logId, "INFO", false, [data:params.uuid])
                Utils.logger(logId, "Activar Aplicacion","Se Activo la Aplicacion", "${params.uuid}")
                return [data:[success:true], status:200]
                
            }catch(e){
                new Logs("Activar Aplicacion","Error en la solicitud", logId, e, [data:[success:false]])
                Utils.logger(logId, "Activar Aplicacion", "Error en la solicitud", "ERROR: ${e.getMessage()}")
                status.setRollbackOnly()
                return TypeError.internalError(logId)
            }
        }
    }
    
    def deactivateApp(params, logId){
        Apps.withTransaction{ status ->
            try{
                new Logs("Desactivar Apliacion","Procesando solicitud", logId, "INFO", true,[data:params.uuid])
                Utils.logger(logId, "Desactivar Aplicacion", "Procesando solicitud", "${params.uuid}")
                def app = Apps.findByUuid(params.uuid)
                
                if(!app){
                    new Logs("Desactivar Aplicacion","No se encontro la aplicacion", logId, "INFO", false, [data:params.uuid])
                    Utils.logger(logId,"Desactivar Aplicacion", "No se encontro la apliacion", "${params.uuid}")
                    return TypeError.informationNotFound(logId)
                }
                
                if(app.status == "Deprecada"){
                    new Logs("Desactivar Aplicacion", "La aplicacion ya esta desactivada", logId, "INFO", false, [data:params.uuid])
                    Utils.logger(logId,"Desactivar Aplicacion", "La aplicacion ya esta desactivada", "${params.uuid}")
                    return TypeError.existingRegister(logId)
                }
                app.status ="Deprecada"
                app.save(failOnError:true, flush:true)
                new Logs("Desactivar Aplicacion", "Se desactivo la aplicacion", logId, "INFO", true, [data:params.uuid])
                Utils.logger(logId, "Desactivar Aplicacion", "Se desactivo la aplicacion", "${params.uuid}")
                return [data:[success:true], status:200]
            
            }catch(e){
                new Logs("Desactivar Aplicacion", "Error en la solicitud", logId, e , [data:[success:false]])
                Utils.logger(logId,"Desactivar Aplicacion", "Error en la solicitud")
                status.setRollbackOnly()
                return TypeError.internalError(logId)
            }
        }
    }
    
    def deleteApp(params, logId){
        Apps.withTransaction{ status ->
            try{
                new Logs("Eliminar Aplicacion", "Procesando solicitud", logId,"INFO",true, [data:params.uuid])
                Utils.logger(logId, "Eliminar Aplicacion", "Procesando solicitud", "${params.uuid}")
                def app = Apps.findByStatusAndUuid("Pendiente", params.uuid)
                
                if(!app){
                    new Logs("Eliminar Aplicacion","No se encontro la aplicacion", logId, "INFO",false, [data:params.uuid])
                    Utils.logger(logId, "Eliminar Aplicacion", "No se encontro la aplicacion", "${params.uuid}")
                    return TypeError.informationNotFound(logId)
                }
                // app.status = "Deprecada"
                app.delete(failOnError:true,flush:true)
                new Logs("Eliminar Aplicacion", "Se elimino la aplicacion", logId, "INFO", true, [data:params.uuid])
                Utils.logger(logId, "Eliminar Aplicacion", "Se elimino la aplicacion", "${params.uuid}")
                return [data:[success:true], status:200]
            }catch(e){
                new Logs("Eliminar Aplicacion","Error en la solicitud", logId, e, [data:[success:false]])
                Utils.logger(logId, "Eliminar Aplicacion", "Error en la solicitud", "ERROR: ${e.getMessage()}")
                status.setRollbackOnly()
                return TypeError.internalError(logId)
            }
            
        }
    
    }
    
    def infoApp(params,logId){
        Apps.withTransaction{ status ->
            try {
                new Logs("Informacion de Aplicacion", "Procesando solicitud", logId,"INFO", true, [data:params.uuid])
                Utils.logger(logId, "Informacion de Aplicacion","Procesando solicitud")
                
                // def service = findBy 
                
                def services = AppConnections.findAllByUuidApp(params.uuid).collect{service ->
                    return[
                        servicio: service.uuidService
                    ]
                }
                
                
                
                  
                // def nameService = Apps.findByUuid(services.uuidService).collect{ app->
                //     return[
                //         servicio: app.name
                //     ]
                // }
                
                println(services)
                
                def app = Apps.findByUuid(params.uuid).collect{ app ->
                    return[
                        nombre: app.name,
                        descripcion: app.description,
                        criticidad: app.criticality,
                        puerto: app.port,
                        version: app.versionApp,
                        tipo: app.type,
                        status: app.status,
                        dominio: app.domain,
                        repositorio: app.urlRepository,
                        fechaDep : app.dateUndeploy,
                        uuid: app.uuid,
                        servicios: services
                    ]                
                }
                
                // def informationDetails = [aplicacion:app, servicios:services]
                
                
                if(!app){
                    new Logs("Informacion de Aplicacion","No se encontro la informacion", logId, "INFO", false, [data:params.uuid])
                    Utils.logger(logId,"Informacion de Aplicacion","No se encontro la informacion")
                    return TypeError.informationNotFound(logId)
                }
                
                new Logs("Informacion del perfil", "Perfil Encontrado", logId, "INFO", true, [data:params.uuid])
                Utils.logger(logId,"Informacion del perfil", "Perfil Encontrado")
                return [data:[success:true,data:app], status:200]
                
            }catch(e){
                new Logs("Informacion de Aplicacion","Error en la solicitud", logId, e, [data:[success:false]])
                Utils.logger(logId, "Informacion de Aplicacion", "Error en la solicitud", "ERROR: ${e.getMessage()}")
                status.setRollbackOnly()
                return TypeError.internalError(logId)
            }                                
        }
    }
    
    def allApps(logId){
        Apps.withTransaction{ status ->
            try{
                new Logs("Lista de todas las Aplicaciones", "Procesando solicitud", logId,"INFO", true, [ : ])
                Utils.logger(logId,"Lista de todas las Aplicaciones","procesando solicitud")
                
                def app = Apps.getAll().collect(){ app ->
                    return [
                        uuid: app.uuid,
                        criticality:app.criticality,
                        name:app.name,
                        type:app.type,
                        status:app.status,
                        description:app.description
                    ]
                }
                def appTotal = Apps.count()
                println(appTotal)
                
                
                new Logs("Lista de todas las Aplicaciones","Apps recuperadas exitosamente", logId,"INFO",true, [data:appTotal])
                Utils.logger(logId, "Lista de todas las Aplicaciones", "Apps recuperadas exitosamente")
                return [data:[success: true, data:[total:appTotal,list:app]], status:200]
            }catch(e){
                new Logs("Lista de todas las Aplicaciones","Error en la solicitud", logId, e, [data:[success:false]])
                Utils.logger(logId, "Lista de todas las Aplicaciones", "Error en la solicitud", "ERROR: ${e.getMessage()}")
                return TypeError.internalError( logId )
            }
        
        }
    }
    
}
