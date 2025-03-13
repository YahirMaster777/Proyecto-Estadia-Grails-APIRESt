package com.ordenaris.internalControl

import grails.gorm.transactions.Transactional
import javax.servlet.ServletContext

@Transactional
class SettingsService {

    def createSetting(String logId, String identifier, String data, ServletContext servletContext) {
        Settings.withTransaction { sStatus ->
            try {
                new Logs("Crear valor parametrizable", "Procesando Solicitud", logId, "INFO", true, [:])
                Utils.logger(logId, "Crear valor parametrizable", "Procesando Solicitud")
                def setting = new Settings(identifier: identifier, data: data)
                setting.save(flush: true, failOnError: true)
                new Logs("Crear valor parametrizable", "Se registró el usuario", logId, "INFO", true, [:])
                Utils.logger(logId, "Crear valor parametrizable", "Se registró el usuario")
                refreshData(servletContext)
                return [data: [success: true], status: 200]
            } catch (Exception e) {
                sStatus.setRollbackOnly()
                new Logs("Crear valor parametrizable", "Error en la solicitud al crear el Token", logId, e, [:])
                Utils.logger(logId, "Crear valor parametrizable", "Error en la solicitud al crear el Token", "f: ${e.message}")
                return TypeError.internalError(logId)
            }
        }
    }

    def updateSetting(String logId, String identifier, String data, ServletContext servletContext) {
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
                refreshData(servletContext)
                new Logs("Actualizar valor parametrizable", "Se actualizó el usuario", logId, "INFO", true, [:])
                Utils.logger(logId, "Actualizar valor parametrizable", "Se actualizó el usuario")
                return [data: [success: true], status: 200]
            } catch (Exception e) {
                sStatus.setRollbackOnly()
                new Logs("Actualizar valor parametrizable", "Error en la solicitud al crear el Token", logId, e, [:])
                Utils.logger(logId, "Actualizar valor parametrizable", "Error en la solicitud al crear el Token", "f: ${e.message}")
                return TypeError.internalError(logId)
            }
        }
    }

    def deleteSetting(String logId, String identifier, ServletContext servletContext) {
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
                refreshData(servletContext)
                return [data: [success: true], status: 200]
            } catch (Exception e) {
                sStatus.setRollbackOnly()
                new Logs("Eliminar valor parametrizable", "Error en la solicitud al crear el Token", logId, e, [:])
                Utils.logger(logId, "Eliminar valor parametrizable", "Error en la solicitud al crear el Token", "f: ${e.message}")
                return TypeError.internalError(logId)
            }
        }
    }

    def refreshData(ServletContext servletContext) {
        try {
            new Logs("Actualizar los valores parametrizables", "Procesando Solicitud", logId, "INFO", true, [:])
            Utils.logger(logId, "Actualizar los valores parametrizables", "Procesando Solicitud")
            def dataMapGlobal = servletContext.getAttribute("dataMapGlobal") as Map<String, String>
            if (dataMapGlobal == null) {
                dataMapGlobal = [:]
                servletContext.setAttribute("dataMapGlobal", dataMapGlobal)
            }
            dataMapGlobal.clear()
            Settings.list().each { setting ->
                dataMapGlobal[setting.identifier] = setting.data
            }
        } catch (Exception e) {
            new Logs("Refrescar datos", "Error en la solicitud al refrescar datos", null, e, [:])
            Utils.logger(null, "Refrescar datos", "Error en la solicitud al refrescar datos", "f: ${e.message}")
            throw new RuntimeException(e)
        }
    }

    def getSetting(String identifier, ServletContext servletContext) {
        def dataMapGlobal = servletContext.getAttribute("dataMapGlobal") as Map<String, String>
        return dataMapGlobal[identifier]
    }
}