package com.ordenaris.internalControl

class Sections {
    Date dateCreated 
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    Date lastUpdated
    String url
    String name
    String description
    String status = 'activa'
    
    
    static mapping = {
        version false
        status sqlType:"Enum('activa','Inactiva', 'mantenimiento', 'Pruebas')"
    }
    static constraints = {
        lastUpdated nullable:true, blank:true
        uuid nullable: false, blank: false, unique: true, maxSize:32
        url nullable: false, blank: false
        name nullable: false, blank: false, maxSize:50
        status nullable: false, inList:["activa","Inactiva", "mantenimiento", "Pruebas"]
        description nullable:false, maxSize:150
    }
}

class SubSections{
    Date dateCreated
    Date lastUpdated
    String uuid =  UUID.randomUUID().toString().replaceAll('\\-', '')
    Sections section
    Sections subSection
    
    static mapping = {
        version false
    }
    
    static constraints = {
        name nullable: false, blank: false, maxSize:50
        uuid nullable: false, blank: false, unique: true, maxSize:32
        url nullable: false, blank: false
    }
}
