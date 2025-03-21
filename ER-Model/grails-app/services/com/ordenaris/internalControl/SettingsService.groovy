package com.ordenaris.internalControl

import grails.gorm.transactions.Transactional

@Transactional
class SettingsService {
    def createSetting(logId, String identifier, String data  ) {
        Settings.withTransaction { sStatus ->
            try {
                new Logs("Crear valor parametrizable", "Procesando Solicitud", logId, "INFO", true, [identificador:identifier, data:data])
                Utils.logger(logId, "Crear valor parametrizable", "Procesando Solicitud", "identificador:$identifier, data:$data")
                def setting = new Settings(identifier: identifier, data: data)
                setting.save(flush: true, failOnError: true)
                refreshSetting(logId)
                new Logs("Crear valor parametrizable", "Se registró el valor parametrizable", logId, "INFO", true, [identificador:identifier])
                Utils.logger(logId, "Crear valor parametrizable", "Se registró el valor parametrizable", "identificador:$identifier")
                return [data: [success: true], status: 200]
            } catch (e) {
                sStatus.setRollbackOnly()
                new Logs("Crear valor parametrizable", "Error al crear el valor parametrizable", logId, e, [identificador:identifier, data:data])
                Utils.logger(logId, "Crear valor parametrizable", "Error al crear el valor parametrizable", "f: ${e.message}")
                return TypeError.internalError(logId)
            }
        }
    }

    def updateSetting(logId, String identifier, String data) {
        Settings.withTransaction { sStatus ->
            try {
                new Logs("Actualizar valor parametrizable", "Procesando Solicitud", logId, "INFO", true, [identificador:identifier, data:data])
                Utils.logger(logId, "Actualizar valor parametrizable", "Procesando Solicitud", "identificador:$identifier, data:$data")
                Settings setting = Settings.findByIdentifier(identifier)
                if (!setting) {
                    new Logs("Actualizar valor parametrizable", "No se encontró el registro", logId, "ERROR", false, [identificador:identifier])
                    Utils.logger(logId, "Actualizar valor parametrizable", "No se encontró el registro", "identificador:$identifier")
                    return TypeError.informationNotFound(logId)
                }
                setting.data = data
                setting.save(flush: true, failOnError: true)
                refreshSetting(logId)
                new Logs("Actualizar valor parametrizable", "Se actualizó el valor parametrizable", logId, "INFO", true, [identificador:identifier])
                Utils.logger(logId, "Actualizar valor parametrizable", "Se actualizó el valor parametrizable", "identificador:$identifier")
                return [data: [success: true], status: 200]
            } catch (e) {
                sStatus.setRollbackOnly()
                new Logs("Actualizar valor parametrizable", "Error al actualizar el valor parametrizable", logId, e, [identificador:identifier, data:data])
                Utils.logger(logId, "Actualizar valor parametrizable", "Error al actualizar el valor parametrizable", "f: ${e.message}")
                return TypeError.internalError(logId)
            }
        }
    }

    def deleteSetting( logId, String identifier) {
        Settings.withTransaction { sStatus ->
            try {
                new Logs("Eliminar valor parametrizable", "Procesando Solicitud", logId, "INFO", true, [identificador:identifier])
                Utils.logger(logId, "Eliminar valor parametrizable", "Procesando Solicitud", "identificador:$identifier")
                Settings setting = Settings.findByIdentifier(identifier)
                if (!setting) {
                    new Logs("Eliminar valor parametrizable", "No se encontró el registro", logId, "ERROR", false, [identificador:identifier])
                    Utils.logger(logId, "Eliminar valor parametrizable", "No se encontró el registro", "identificador:$identifier")
                    return TypeError.informationNotFound(logId)
                }
                setting.delete()
                refreshSetting(logId)
                new Logs("Eliminar valor parametrizable", "Se elimino el valor parametrizable", logId, "INFO", true, [identificador:identifier])
                Utils.logger(logId, "Eliminar valor parametrizable", "Se elimino el valor parametrizable", "identificador:$identifier")
                return [data: [success: true], status: 200]
            } catch (e) {
                sStatus.setRollbackOnly()
                new Logs("Eliminar valor parametrizable", "Error al eliminar el valor parametrizable", logId, e, [identificador:identifier])
                Utils.logger(logId, "Eliminar valor parametrizable", "Error al eliminar el valor parametrizable", "f: ${e.message}")
                return TypeError.internalError(logId)
            }
        }
    }

    @Transactional(readOnly = true)
    def refreshSetting(logId) {
        try {
            new Logs("Refrescar datos parametrizables", "Procesando Solicitud", logId, "INFO", true, [:])
            Utils.logger(logId, "Refrescar datos parametrizables", "Procesando Solicitud")
            def listSetting = [:]
            Settings.list().each { setting ->
                listSetting[setting.identifier] = setting.data
            }
            Setting.set(listSetting, logId)
            new Logs("Refrescar datos parametrizables", "Valores parametrizable actualizados", logId, "INFO", true, [:])
            Utils.logger(logId, "Refrescar datos parametrizables", "Valores parametrizable actualizados")
            return [data: [success: true], status: 200]
        } catch (e) {
            new Logs("Refrescar datos parametrizables", "Error en la solicitud al refrescar los datos parametrizables", null, e, [:])
            Utils.logger(logId, "Refrescar datos parametrizables", "Error en la solicitud al refrescar los datos parametrizables", "f: ${e.message}")
            return TypeError.internalError(logId)
        }
    }  
}