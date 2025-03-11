package com.ordenaris.internalControl

import grails.gorm.transactions.Transactional

@Transactional
class SettingsService {

    def createSetting(String identifier, String data) {
        println "creando una variable global"
        new Settings(data: data, identifier: identifier).save(flush: true, failOnError: true)
    }
    def updateSetting(String identifier, String data) {
        println "Actualizando una variable global"
        Settings setting = Settings.findByIdentifier(identifier)
        if (setting) {
            setting.data = data
            setting.save(flush: true, failOnError: true)
        }
        refreshData()
    }
    def deleteSetting(String identifier) {
        println "Eliminando una variable global"
        Settings setting = Settings.findByIdentifier(identifier)
        if (setting) {
            setting.delete()
        }
        refreshData()
    }

    def refreshData() {
        def dataMapGlobal = servletContext.getAttribute(dataMapGlobal)
        Settings.list().each { setting ->
            dataMapGlobal[setting.identifier] = setting.data
        }
        println "Estoy en el setting la variable global"
        // BootStrap.dataMapGlobal = dataMapGlobal
    }
    void initializeDefaults() {
        Settings.initializeDefaults()
    }
}
