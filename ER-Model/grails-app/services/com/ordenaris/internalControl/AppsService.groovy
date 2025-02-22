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
    
    def deleteApp(data, logId){
        Apps.withTransaction{ status ->
            new Logs("Eliminar Aplicacion", "Procesando solicitud", logId,"INFO",true, [data:params.uuid])
            Utils.logger(logId, "Eliminar Aplicacion", "Procesando solicitud")
            def app = findByStatuAndUuid("Activa")
        
        }
    
    }
    
}
