package com.ordenaris.internalControl
class Apps {
    static hasMany = [serverApp: ServersApps]
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    String name
    String urlRepository
    String versionApp
    String status = "Pendiente"
    String port
    String domain
    String type
    String criticality
    Date dateCreated
    Date lastUpdated
    Date dateUndeploy
    String description
    
    static mapping ={
        version false
        type sqlType:"Enum('Frontend','Backend','Aplication','Data Base')"
        criticality sqlType: "Enum('Indiferente', 'Baja', 'Media', 'Alta', 'Critica')"
        status sqlType : "Enum('Activa','Depracada','Pendiente','Desarollo')"

    }   
    static constraints = {
        urlRepository nullable:true, maxSize:150, blank:true
        type inList:['Frontend','Backend','Aplication','Data Base']
        dateUndeploy nullable:true, blank:true
        domain nullable:true, maxSize:150
        status inList: ['Activa','Depracada','Pendiente','Desarollo']
        criticality inList: ["Indiferente", "Baja", "Media", "Alta", "Critica"], blank: true, nullable:true
        versionApp nullable:true, maxSize:20
        uuid maxSize:32, unique:true
        port blank:true, nullable:true,maxSize:5
        description maxSize:150
        name maxSize:50
    }
}

class AppConnections{
    Apps app
    Apps service
    String description
    String portApp
    String portService
    Date dateCreated
    Date lastUpdated
    static mapping = {
        version false
    }
    static constraints ={
        description maxSize:150, nullable:true, blank:true
        portApp maxSize:5, nullable:true, blank:true
        portService maxSize:5, nullable:true, blank:true
    }
    
    
}