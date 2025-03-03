package com.ordenaris.internalControl

class IntentRecovery {
    Date dateCreated
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    String host
    String used = 'Activo'
    Users user
    int intents
    Date dateExpired
    
    static mapping = {
        version false
        used sqlType:"Enum('Activo', 'Inactivo')"
    }
    static constraints = {
        host blank:true, nullable:true,maxSize:15
        used inList:['Activo', 'Inactivo']
        uuid maxSize:32, unique:true
        dateExpired nullable:true, blank:true
    }
}
