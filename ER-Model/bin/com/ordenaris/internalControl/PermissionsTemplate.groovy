package com.ordenaris.internalControl

class TemplatePermissions {
    Date dateCreated
    Date lastUpdated
    Permissions permission
  
    static belongsTo=[template : Templates]
    
    static mapping = {
        version false
    }
}

class Templates {
    String uuid = UUID.randomUUID().toString().replaceAll('\\-','')
    Date dateCreated
    Date lastUpdated
    String name
    String status = "Activo"
    String description
    
    static hasMany=[permissions:TemplatePermissions]
    
    static mapping = {
        version false
        status sqlType : "Enum('Activo', 'Inactivo')"
        permissions cascade: 'all-delete-orphan'
    }
    static constraints = {
        status inList:["Activo", "Inactivo"]
        uuid unique:true, maxSize:32
        name maxSize:50, unique:true
        description maxSize:150
    }
    
}
