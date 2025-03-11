package com.ordenaris.internalControl

class Settings {
    String identifier
    String data
    
    static constraints = {
        identifier maxSize:50
    }
    static mapping = {
        version false
    }
    // public static final NUMBER_OF_RECOVERY_ATTEMPTS = new Settings(data: '3', identifier: 'NUMBER_OF_RECOVERY_ATTEMPTS').save(flush:true)
    // public static final MINUTES_OF_VALIDITY_CODE = new Settings(data: '30', identifier: 'MINUTES_OF_VALIDITY_CODE').save(flush:true)
    // public static void initializeDefaults() {
    //     if (!Settings.findByIdentifier('NUMBER_OF_RECOVERY_ATTEMPTS')) {
    //         .save(flush: true, failOnError: true)
    //     }
    //     if (!Settings.findByIdentifier('MINUTES_OF_VALIDITY_CODE')) {
    //        .save(flush: true, failOnError: true)
    //     }
    // }

    public static initializeDefaults() {
        def registers = [
            new Settings(data: '3', identifier: 'NUMBER_OF_RECOVERY_ATTEMPTS'),
            new Settings(data: '30', identifier: 'MINUTES_OF_VALIDITY_CODE')
        ]
        Settings.saveAll(registers)
    }
    
    def SettingsService
    public static Settings createSetting(String identifier, String data) {
        SettingsService.createSetting(identifier, data)
    }
    public static Settings updateSetting(String identifier, String data) {
        SettingsService.updateSetting(identifier, data)        
    }
    public static void deleteSetting(String identifier) {
        SettingsService.deleteSetting(identifier)
    }
    public static void refreshData() {
        SettingsService.refreshData()
    }
}