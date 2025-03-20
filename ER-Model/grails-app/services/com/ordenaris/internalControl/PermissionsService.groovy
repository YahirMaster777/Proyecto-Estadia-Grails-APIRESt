package com.ordenaris.internalControl

import grails.gorm.transactions.Transactional

@Transactional
class PermissionsService {

    
    def createPermission(data, logId){
        Permissions.withTransaction{ status ->
            try{
                new Logs("Registrar Permiso", "Procesando solicitud",logId, "INFO", true, [ : ])
                Utils.logger(logId, "Registrar Permiso", "Procesando solicitud")
                
                def permissionExists = Permissions.findByAlias(data.alias)
                if(permissionExists){
                    new Logs("Registrar Permiso", "Ya existe un registro con esos datos", logId, "INFO", false, [ : ])
                    Utils.logger(logId, "Registrar Permiso", "Ya existe un registro con esos datos")
                    return TypeError.existingRegister(logId)
                }
                
                def section = Sections.findByName(data.section)
                if(!section){
                    new Logs("Registrar Permiso", "Seccion no encontrada", logId, "INFO", false, [ : ])
                    Utils.logger(logId ,"Registrar Permiso", "Seccion no encontrada")
                    return TypeError.informationNotFound()
                }
                
                def permission = new Permissions()
                permission.section = section
                permission.name = data.name
                permission.alias = data.alias
                permission.description = data.description
                permission.save(flush:true, failOnError:true)
                new Logs("Registrar Permiso", "Se registro el permiso", logId, "INFO", true, [ : ])
                Utils.logger(logId, "Registrar Permiso", "Se registro el permiso", "Permiso: ${data.name}")
                return [data:[succes:true],status:200]
                
            }catch(e){
                new Logs("Registrar Permiso", "Error en la peticion", logId, e, [ : ])
                Utils.logger(logId, "Registrar Permiso", "Error en la peticion", "ERROR: ${e.getMessage()}")
                status.setRollbackOnly()
                return TypeError.internalError(logId)
            }
        }   
    }

    def deletePermission(params, logId){
        Permissions.withTransaction{status -> 
            try{ 
                new Logs("Eliminar Permiso", "Procesando solicitud", logId, "INFO", true, [ : ])
                Utils.logger(logId, "Eliminar Permiso", "Procesando solicitud")
                def permission = Permissions.findByUuid(params.uuid)
                if(!permission){
                    new Logs("Eliminar Permiso", "No se encontro el permiso",logId, "INFO", false, [ : ])
                    Utils.logger(logId, "Eliminar Permiso", "No se encontro el permiso")
                    return TypeError.informationNotFound(logId)
                }
                
                permission.uuid = "_delete_"+ new Date().log()
                permission.save(flush:true, failOnError:true)
                new Logs("Eliminar Permiso", "Se elimino el permiso", logId, "INFO", true,  [ : ])
                Utils.logger(logId, "Eliminar Permiso", "Se elimino el permiso")
                return [data:[succes:true],status:200]
            }catch(e){
                new Logs("Eliminar Permiso","Error en la solicitud", logId, e, [ : ])
                Utils.logger(logId,"Eliminar Permiso", "Error en la solicitud", "ERROR: ${e.getMessage()}")
                status.setRollbackOnly()
                return TypeError.internalError(logId)
            }
        }
    }
    
    def allPermissions(logId){
        Permissions.withTransaction{ status-> 
            try{
                new Logs("Lista de Permisos", "Procesando solicitud", logId, "INFO", true, [ : ])
                Utils.logger(logId, "Lista de Permisos", "Procesando solictud")
                
                def permissions = Permissions.findAll().collect(){ p ->
                    return [
                        nombre: p.name,
                        alias : p.alias,
                        uuid  : p.uuid,
                        descripcion : p.description
                    ]
                }
                new Logs("Lista de Permisos", "Lista de permisos obtenida", logId, "INFO", true, [ : ])
                Utils.logger(logId,"Lista de Permisos", "Lista de Permisos obtenida")
                return [data:[succes:true, data:[permisos:permissions]]]
            
            }catch(e){
                new Logs("Lista de Permisos", "Error en la solicitud",logId, e, [ : ])
                Utils.logger(logId,"Lista de Permisos", "Error en la solicitud", "ERROR: ${e.getMessage()}")
                status.setRollbackOnly()
                return TypeError.internalError(logId)
            }
        }
    }
    
    def changeStatus(params,logId){
        Permissions.withTransaction{ status ->
            try{
                new Logs("Cambiar status del permiso", "Procesando solicitud", logId, "INFO",true, [ : ])
                Utils.logger(logId, "Cambiar status del permiso", "Procesando solicitud")
                def permission = Permissions.findByUuid(params.uuid)
                
                if(!permission){
                    new Logs("Cambiar status del permiso","Error en la solicitud", logId, "INFO", false, [ : ])
                    Utils.logger(logId, "Cambiar status del permiso", "Error en la solicitud")
                    return TypeError.internalError(logId)
                }
                
                if(params.actionService == "activate"){
                    new Logs("Cambiar status del permiso", "Activar Permiso", logId, "INFO", true, [ : ])
                    Utils.logger(logId, "Cambiar status del permiso", "Activar Permiso")
                    
                    if(permission.status == 'activo'){
                        new Logs("Cambiar status del permiso", "El permios ya esta activo", logId, "INFO", false, [ : ])
                        Utils.logger(logId, "Cambiar status del permiso", "El permiso ya esta activo")
                        return TypeError.existingRegister(logId)
                    }
                    
                    permission.status = 'activo'
                    permission.save(failOnError:true, flush:true)           
                    new Logs("Cambiar status del permiso", "Se activo el Permiso", logId, "INFO", true, [ : ])
                    Utils.logger(logId, "Cambiar status del permiso", "Se activo el Permiso")
                }
                
                if(params.actionService == 'deactivate'){
                    new Logs("Cambiar status del permiso", "Desactivar Permiso", logId, "INFO", true, [ : ])
                    Utils.logger(logId, "Cambiar status del permiso", "Desactivar Permiso")
                    
                    if(permission.status == 'inactivo'){
                        new Logs("Cambiar status del permiso", "El permiso ya esta inactivo", logId, "INFO", false , [ : ])
                        Utils.logger(logId, "Cambiar status del permiso", "El permiso ya esta inactivo")
                        return TypeError.existingRegister(logId)
                    }
                    
                    permission.status = 'inactivo'
                    permission.save(failOnError:true, flush:true)
                    new Logs("Cambiar status del permiso", "Se desactivo el Permiso", logId, "INFO", true, [ : ])
                    Utils.logger(logId, "Cambiar status del permiso", "Se desactivo el permiso")
                }
                
                return [data:[succes:true], status:200]
                
                
            }catch(e){
                new Logs("Cambiar status del permiso", "Error en la solicitud", logId, e, [ : ])
                Utils.logger(logId, "Cambiar status del permiso", "Error en la solicitud", "ERROR : ${e.getMessage()}")
                status.setRollbackOnly()
                return TypeError.internalError()
            }
        }
    }
    
    def infoPermission(params, logId){
        Permissions.withTransaction{ status -> 
            try{
                new Logs("Informacion Permiso", "Procesando solicitud", logId, "INFO", true, [ : ])
                Utils.logger(logId, "Informacion Permiso", "Procesando solicitud")
                
                def permission = Permissions.findByUuid(params.uuid).collect(){ p ->
                    return[
                       nombre : p.name,
                       uuid   : p.uuid,
                       alias  : p.alias,
                       descripcion : p.description
                    ]
                }
                
                if(!permission){
                    new Logs("Informacion Permiso", "No se encontro el permiso", logId, "INFO", false, [ : ])
                    Utils.logger(logId, "Informacion Permiso", "No se encontro el permiso")
                    return TypeError.informationNotFound(logId)
                }
                
                new Logs("Informacion Permiso", "Se recupero el permiso", logId, "INFO", true, [data: permission.name])
                Utils.logger(logId,"Informacion Permiso", "Se recupero el permiso")
                
                return [data:[succes:true, data:permission]]
                
            }catch(e){
                new Logs("Informacion Permiso", "Error en la solicitud", logId, e, [ : ])
                Utils.logger(logId, "Informacion Permiso", "Error en la solicitud", "ERROR : ${e.getMessage()}")
                status.setRollbackOnly()
                return TypeError.internalError(logId)
            }
        }
    }
    
    
    
}
