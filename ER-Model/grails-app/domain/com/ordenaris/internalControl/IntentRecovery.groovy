package com.ordenaris.internalControl

class IntentRecovery {
    Date dateCreated
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    String host
    String used
    Users user
    
    static mapping = {
        version false
        used sqlType:"Enum('Activo', 'Inactivo')"
    }
    static constraints = {
        host blank:true, nullable:true,maxSize:15
        uuid maxSize:32, unique:true
    }
}
