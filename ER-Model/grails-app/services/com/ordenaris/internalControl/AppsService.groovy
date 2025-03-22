package com.ordenaris.internalControl

import grails.gorm.transactions.Transactional

@Transactional
class AppsService {

    def createApp(data, logId){
        Apps.withTransaction{ status-> 
            try{    
                
                
                new Logs("Registrar Aplicacion", "Procesando solicitud",logId, "INFO", true, [data:data.name])
                Utils.logger(logId, "Registrar Aplicacion", "Procesando solicitud")
                
                def appExists = Apps.findByNameAndType(data.name, data.type)
                if(appExists){
                    new Logs("Registrar Aplicacion", "Ya existe un registro", logId, "INFO", false, [data:appExists.uuid])
                    Utils.logger(logId, "Registrar Aplicacion", "Ya existe un registro", "Regitro: ${appExists.uuid}" )
                    return TypeError.existingRegister(logId)
                }
                
                def aplication = new Apps()
                aplication.criticality = data.criticality
                aplication.dateUndeploy = data.dateUndeploy
                aplication.name = data.name
                aplication.type = data.type
                aplication.urlRepository = data.urlRepository
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
    
    
    def statusManagement(params, logId){
        Apps.withTransaction{ status -> 
            try{
                new Logs("Cambio de status", "Procesando solicitud", logId, "INFO", true, [ : ])
                Utils.logger(logId,"Cambio de status", "Procesando solicitud")
                
                def app = Apps.findByUuid(params.uuid)
                println app.status
                if(!app){
                    new Logs("Cambio de status","No se encontro la informacion solicitada",logId, "INFO", false, [ : ])
                    Utils.logger(logId,"Cambio de status", "No se encontro la informacion solicitda")
                    return TypeError.informationNotFound(logId)
                }
                println "action: " + params.actionService
                
                if(params.actionService == "activate"){
                    new Logs("Cambio de status", "Activar App", logId, "INFO", true, [ : ])
                    Utils.logger(logId, "Cambio de status", "Activar App")
                    app.status = "activa"
                    app.save(flush:true, failOnError:true)
                    new Logs("Cambio de status", "Se activo la app", logId, "INFO", true, [ : ])
                    Utils.logger(logId, "Cambio de status", "Se activo la app")
                   
                }
                
                if(params.actionService == "deactivate"){
                    new Logs("Cambio de status", "Se desactivo la App", logId, "INFO", true, [ : ])
                    Utils.logger(logId, "Cambio de status", "Se desactivo la App")
                    app.status = "deprecada"
                    app.save(flush:true, failOnError:true)
                    new Logs("Cambio de status", "Se desactivo la App", logId, "INFO", true, [ : ])
                    Utils.logger(logId, "Cambio de status", "Se desactivo la App")
                }
                
                return [data:[success:true], status:200]
                
            }catch(e){
                new Logs("Cambio de status", "Error en la solicitud", logId, e, [ : ])
                Utils.logger(logId, "Cambio de status", "Error en la solicitud", "ERROR: ${e.getMessage()}")
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
                def app = Apps.findByUuid(params.uuid)
                
                if(!app){
                    new Logs("Eliminar Aplicacion","No se encontro la aplicacion", logId, "INFO",false, [data:params.uuid])
                    Utils.logger(logId, "Eliminar Aplicacion", "No se encontro la aplicacion", "${params.uuid}")
                    return TypeError.informationNotFound(logId)
                }
                app.uuid = "_delete_"+new Date().log()
                app.status = "deprecada"
                app.save(failOnError:true,flush:true)
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
                def app2 = Apps.findByUuid(params.uuid)
                def services = AppConnections.findAllByApp(app2).collect{service ->
                    return[
                        servicio: service
                    ]
                }
                def app1 = services[0]
                
                println app1
                println app1.name
                
                def service = AppConnections.findByApp(app1)                            
                println service
                def app = Apps.findByUuid(params.uuid).collect{ app ->
                    return[
                        nombre: app.name,
                        descripcion: app.description,
                        criticidad: app.criticality,
                        tipo: app.type,
                        status: app.status,
                        repositorio: app.urlRepository,
                        fechaDep : app.dateUndeploy,
                        uuid: app.uuid
                    ]                
                }                
                
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
                new Logs("Lista de Aplicaciones", "Procesando solicitud", logId,"INFO", true, [ : ])
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
                
                
                new Logs("Lista de Aplicaciones","Apps recuperadas exitosamente", logId,"INFO",true, [data:appTotal])
                Utils.logger(logId, "Lista de Aplicaciones", "Apps recuperadas exitosamente")
                return [data:[success: true, data:[total:appTotal,list:app]], status:200]
            }catch(e){
                new Logs("Lista de Aplicaciones","Error en la solicitud", logId, e, [data:[success:false]])
                Utils.logger(logId, "Lista de Aplicaciones", "Error en la solicitud", "ERROR: ${e.getMessage()}")
                return TypeError.internalError( logId )
            }
        
        }
    }
    
}
