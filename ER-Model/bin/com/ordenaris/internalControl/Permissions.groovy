package com.ordenaris.internalControl

class Permissions {
    Date dateCreated
    String name
    String description
    String alias
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    Sections section
    String status = 'Activo'
    
    static mapping ={
        status sqlType : "Enum('Activo','Inactivo')"        
        version false
    }
    static constraints = {
        uuid unique:true, maxSize:32
        status inList:['Activo', 'Inactivo']
        name maxSize:50
        description maxSize:150
        alias maxSize:50, unique: true
    }
}
