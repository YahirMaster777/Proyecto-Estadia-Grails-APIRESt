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
                new Logs("Activar/Desactivar Aplicacion", "Procesando solicitud", logId, "INFO", true, [data:params.uuid])
                Utils.logger(logId,"Activar/Desactivar Aplicacion", "Procesando solicitud")
                def app = Apps.findByUuid(params.uuid)
                
                if(!app){
                    new Logs("Activar/Desactivar Aplicacion", "No se encontro la aplicacion", logId, "INFO", false, [data:params.uuid])
                    Utils.logger(logId, "Activar/Desactivar Aplicacion","No se encontro la aplicacion")
                    return TypeError.informationNotFound(logId)
                }
                
                if(app.status == "Activa"){
                    new Logs("Activar/Desactivar Aplicacion", "Ya esta Activa", logId, "INFO", false, [data:params.uuid])
                    Utils.logger(logId, "Activar/Desactivar Aplicacion","Ya esta Activa")
                    return TypeError.informationNotFound(logId)
                }
                
                print(app.status)
                app.status="Activa"
                app.save(failOnError:true, flush:true)
                new Logs("Activar/Desactivar Aplicacion", "Se Activo la Aplicacion", logId, "INFO", false, [data:params.uuid])
                Utils.logger(logId, "Activar/Desactivar Aplicacion","Se Activo la Aplicacion")
                return [data:[success:true], status:200]
                
            }catch(e){
                new Logs("Activar/Desactivar Aplicacion","Error en la solicitud", logId, e, [data:[success:false]])
                Utils.logger(logId, "Activar/Desactivar Aplicacion", "Error en la solicitud", "ERROR: ${e.getMessage()}")
                status.setRollbackOnly()
                return TypeError.internalError(logId)
            }
        }
    }
    
    def deleteApp(params, logId){
        Apps.withTransaction{ status ->
            try{
                new Logs("Eliminar Aplicacion", "Procesando solicitud", logId,"INFO",true, [data:params.uuid])
                Utils.logger(logId, "Eliminar Aplicacion", "Procesando solicitud")
                def app = Apps.findByStatusAndUuid("Pendiente", params.uuid)
                
                if(!app){
                    new Logs("Eliminar Aplicacion","No se encontro la aplicacion", logId, "INFO",false, [data:params.uuid])
                    Utils.logger(logId, "Eliminar Aplicacion", "No se encontro la aplicacion")
                    return TypeError.informationNotFound(logId)
                }
                // app.status = "Deprecada"
                app.delete(failOnError:true,flush:true)
                new Logs("Eliminar Aplicacion", "Se elimino la aplicacion", logId, "INFO", true, [data:params.uuid])
                Utils.logger(logId, "Eliminar Aplicacion", "Se elimino la aplicacion")
                return [data:[success:true], status:200]
            }catch(e){
                new Logs("Eliminar Aplicacion","Error en la solicitud", logId, e, [data:[success:false]])
                Utils.logger(logId, "Eliminar Aplicacion", "Error en la solicitud", "ERROR: ${e.getMessage()}")
                status.setRollbackOnly()
                return TypeError.internalError(logId)
            }
            
        }
    
    }
    
}
