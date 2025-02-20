package com.ordenaris.internalControl

import grails.gorm.transactions.Transactional

@Transactional
class AppsService {

    def createApp(data, logId){
        Apps.withTransaction{ status-> 
            try{
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
                aplication.save(failOnError:true, flush:true)
                return [data:[success:true],status:200]
            }catch(e){
                new Logs("Registrar Aplicacion","Error en la solicitud", logId, e, [data:[success:false]])
                Utils.logger(logId, "Registrar Aplicacion", "Error en la solicitud", "ERROR: ${e.getMessage()}")
                status.setRollbackOnly()
                return TypeError.internalError(logId)
            }
        } 
    }
    
}
