package com.ordenaris.internalControl

import grails.gorm.transactions.Transactional

@Transactional
class SettingsService {
    def createSetting(logId, String identifier, String data  ) {
        Settings.withTransaction { sStatus ->
            try {
                new Logs("Crear valor parametrizable", "Procesando Solicitud", logId, "INFO", true, [:])
                Utils.logger(logId, "Crear valor parametrizable", "Procesando Solicitud")
                def setting = new Settings(identifier: identifier, data: data)
                setting.save(flush: true, failOnError: true)
                refreshSetting(logId)
                new Logs("Crear valor parametrizable", "Se registró el usuario", logId, "INFO", true, [:])
                Utils.logger(logId, "Crear valor parametrizable", "Se registró el usuario")
                return [data: [success: true], status: 200]
            } catch (Exception e) {
                sStatus.setRollbackOnly()
                new Logs("Crear valor parametrizable", "Error en la solicitud al crear el Token", logId, e, [:])
                Utils.logger(logId, "Crear valor parametrizable", "Error en la solicitud al crear el Token", "f: ${e.message}")
                return TypeError.internalError(logId)
            }
        }
    }

    def updateSetting(logId, String identifier, String data) {
        Settings.withTransaction { sStatus ->
            try {
                new Logs("Actualizar valor parametrizable", "Procesando Solicitud", logId, "INFO", true, [:])
                Utils.logger(logId, "Actualizar valor parametrizable", "Procesando Solicitud")
                Settings setting = Settings.findByIdentifier(identifier)
                if (!setting) {
                    new Logs("Actualizar valor parametrizable", "No se encontró el registro", logId, "ERROR", false, [:])
                    Utils.logger(logId, "Actualizar valor parametrizable", "No se encontró el registro")
                    return TypeError.informationNotFound(logId)
                }
                setting.data = data
                setting.save(flush: true, failOnError: true)
                refreshSetting(logId)
                new Logs("Actualizar valor parametrizable", "Se actualizó el valor", logId, "INFO", true, [:])
                Utils.logger(logId, "Actualizar valor parametrizable", "Se actualizó el valor")
                return [data: [success: true], status: 200]
            } catch (Exception e) {
                sStatus.setRollbackOnly()
                new Logs("Actualizar valor parametrizable", "Error en la solicitud al crear el Token", logId, e, [:])
                Utils.logger(logId, "Actualizar valor parametrizable", "Error en la solicitud al crear el Token", "f: ${e.message}")
                return TypeError.internalError(logId)
            }
        }
    }

    def deleteSetting( logId, String identifier) {
        Settings.withTransaction { sStatus ->
            try {
                new Logs("Eliminar valor parametrizable", "Procesando Solicitud", logId, "INFO", true, [:])
                Utils.logger(logId, "Eliminar valor parametrizable", "Procesando Solicitud")
                Settings setting = Settings.findByIdentifier(identifier)
                if (!setting) {
                    new Logs("Eliminar valor parametrizable", "No se encontró el registro", logId, "ERROR", false, [identifier:identifier])
                    Utils.logger(logId, "Eliminar valor parametrizable", "No se encontró el registro")
                    return TypeError.informationNotFound(logId)
                }
                setting.delete()
                refreshSetting(logId)
                new Logs("Eliminar valor parametrizable", "Se elimino el valor", logId, "INFO", true, [:])
                Utils.logger(logId, "Eliminar valor parametrizable", "Se elimino el valor")
                return [data: [success: true], status: 200]
            } catch (Exception e) {
                sStatus.setRollbackOnly()
                new Logs("Eliminar valor parametrizable", "Error en la solicitud al crear el Token", logId, e, [:])
                Utils.logger(logId, "Eliminar valor parametrizable", "Error en la solicitud al crear el Token", "f: ${e.message}")
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
            Setting.set(listSetting)
            new Logs("Refrescar datos parametrizables", "Valores actualizados", logId, "INFO", true, [:])
            Utils.logger(logId, "Refrescar datos parametrizables", "Valores actualizados")
            return [data: [success: true], status: 200]
        } catch (Exception e) {
            new Logs("Refrescar datos parametrizables", "Error en la solicitud al refrescar los datos", null, e, [:])
            Utils.logger(logId, "Refrescar datos parametrizables", "Error en la solicitud al refrescar los datos", "f: ${e.message}")
            throw new RuntimeException(e)
        }
    }  
}