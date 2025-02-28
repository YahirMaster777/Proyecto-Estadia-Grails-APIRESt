package com.ordenaris.internalControl

import grails.gorm.transactions.Transactional

@Transactional
class AppConnectionsService {

    def createConection(data, logId){
        AppConnections.withTransaction{ status-> 
            try{
                new Logs("Registrar Conexiones de Apps", "Procesando solicitud",logId, "INFO", true, [ : ])
                Utils.logger(logId, "Registrar Conexiones de Apps", "Procesando solicitud")
                
                
                
                def app = Apps.findByName(data.app)
                if(!app){
                    new Logs("Registrar Conexiones de Apps", "No se encontro la App", logId, "INFO", false, [ : ])
                    Utils.logger(logId,"Registrar Conexiones de Apps", "No se encontro la App")
                    return TypeError.informationNotFound(logId)
                }
                def service = Apps.findByName(data.service)
                if(!service){
                    new Logs("Registrar Conexiones de Apps", "No se encontro la App-Servicio", logId, "INFO", false, [ :])
                    Utils.logger(logId,"Registrar Conexiones de Apps", "No se encontro la App-Servicio")
                    return TypeError.informationNotFound(logId)
                }
                
                def appExists = AppConnections.findByUuidAppAndUuidService(app.uuid, service.uuid)
                if(appExists){
                    new Logs("Registrar Conexiones de Apps", "Ingrese otros Valores", logId, "INFO", false, [ : ])
                    Utils.logger(logId,"Registrar Conexiones de Apps", "Ingrese otros Valores", )
                    return TypeError.existingRegister(logId)
                }
                
                def conection = new AppConnections()
                conection.uuidApp = app.uuid
                conection.app = app
                conection.service = service
                conection.uuidService = service.uuid
                conection.portApp = data.portApp
                conection.portService = data.portService
                conection.description = data.description
                
                conection.save(failOnError:true, flush:true)
                new Logs("Registrar Conexiones de Apps","Se registro la conexion", logId, "INFO", true,[data:data.name])
                Utils.logger(logId, "Registrar Conexiones de Apps","Se registro la conexion", "Nombre:${data.name}")
                return [data:[success:true],status:200]
            }catch(e){
                new Logs("Registrar Conexiones de Apps","Error en la solicitud", logId, e, [data:[success:false]])
                Utils.logger(logId, "Registrar Conexiones de Apps", "Error en la solicitud", "ERROR: ${e.getMessage()}")
                status.setRollbackOnly()
                return TypeError.internalError(logId)
            }
        } 
    }
    
}
