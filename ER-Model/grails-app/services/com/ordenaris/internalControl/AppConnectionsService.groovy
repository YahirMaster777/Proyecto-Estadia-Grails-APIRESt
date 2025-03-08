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
                
                def appExists = AppConnections.findByAppAndService(app, service)
                if(appExists){
                    new Logs("Registrar Conexiones de Apps", "Ingrese otros Valores", logId, "INFO", false, [ : ])
                    Utils.logger(logId,"Registrar Conexiones de Apps", "Ingrese otros Valores", )
                    return TypeError.existingRegister(logId)
                }
                
                def conection = new AppConnections()
                conection.app = app
                conection.service = service
                conection.portApp = data.portApp
                conection.portService = data.portService
                conection.description = data.description
                
                conection.save(failOnError:true, flush:true)
                new Logs("Registrar Conexiones de Apps","Se registro la conexion", logId, "INFO", true,[ : ])
                Utils.logger(logId, "Registrar Conexiones de Apps","Se registro la conexion")
                return [data:[success:true],status:200]
            }catch(e){
                new Logs("Registrar Conexiones de Apps","Error en la solicitud", logId, e, [data:[success:false]])
                Utils.logger(logId, "Registrar Conexiones de Apps", "Error en la solicitud", "ERROR: ${e.getMessage()}")
                status.setRollbackOnly()
                return TypeError.internalError(logId)
            }
        } 
    }
    
    def deleteConnection(params, logId){
        AppConnections.withTransaction{status ->
            try{
                new Logs("Eliminar Conexion APP","Procesando solicitud", logId, "INFO", true, [ : ])
                Utils.logger(logId,"Eliminar Conexion APP", "Procesando solicitud")
                def conection = AppConnections.findByUuid(params.uuid)
                if(!conection){
                    new Logs("Eliminar Conexion APP", "No se encontro la informacion solicitda", logId, "INFO", false, [ : ])
                    Utils.logger(logId, "Eliminar Conexion APP", "No se encontro la informacion solicitada")
                    return TypeError.informationNotFound(logId)
                }
                conection.delete(failOnError:true, flush:true)
                new Logs("Eliminar Conexion APP", "Se Elimino la conexion ", logId, "INFO", true, [data:[params.uuid]])
                Utils.logger(logId, "Eliminar Conexion APP", "Se elimino la conexin", "${params.uuid}")
                return [data:[success:true], status:200]
            }catch(e){
                new Logs("Eliminar Conexion APP", "Error en la solicitud", logId, e, [data:[success:false]])
                Utils.logger(logId, "Eliminar Conexion APP","Error en la solicitud", "ERROR: ${e.getMessage()}")
                status.setRollbackOnly()
                return TypeError.internalError(logId)
            }
        }
    }
    
    def updateConnection(params,data, logId){
        AppConnectionsService.withTransaction{status ->
            try{
                new Logs("Editar Conexion APP", "Procesando solicitud", logId, "INFO", true, [ : ])
                Utils.logger(logId,"Editar Conexion APP","Procesando solicitud")
                def conection = AppsConnections.findByUuid(params.uuid)
                if(!conection){
                    new Logs("Editar Conexion APP", "No se encontro la informacion solicitada", logId, "INFO", false, [ : ])
                    Utils.logger(logId, "Editar Conexion APP", "No se encontro la informacion solicitada")
                    return TypeError.informationNotFound
                }
                
                def app= Apps.findByName(data.app)
                if(!app){
                    new Logs("Editar Conexion APP", "No se encontro la informacion solicitada", logId, "INFO", false, [ : ])
                    Utils.logger(logId, "Editar Conexion App","No se encontro la informacion solicitada")
                    return TypeError.informationNotFound(logId)
                }
                def service = Apps.findByName(data.service)
                if(!service){
                    new Logs("Editar Conexion APP", "No se encontro la informacion solicitada", logId, "INFO", false, [ : ])
                    Utils.logger(logId,"Editar Conexion APP", "No se encontro la informacion solicitada")
                    return TypeError.informationNotFound(logId)
                }
                
                
                
            }catch(e){
            
            }
        }
    }
    
}
