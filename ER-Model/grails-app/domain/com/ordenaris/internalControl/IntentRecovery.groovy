package com.ordenaris.internalControl

class IntentRecovery {
    Date dateCreated
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    String used = 'activo'
    Users user
    int intents
    Date dateExpired
    
    static mapping = {
        version false
        used sqlType:"Enum('activo', 'inactivo')"
    }
    static constraints = {
        used inList:['activo', 'inactivo']
        uuid maxSize:32, unique:true
        dateExpired nullable:true, blank:true
    }
}
