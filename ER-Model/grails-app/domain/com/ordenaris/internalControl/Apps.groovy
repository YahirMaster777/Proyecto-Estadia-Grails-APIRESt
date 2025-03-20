package com.ordenaris.internalControl
class Apps {
    static hasMany = [serverApp: ServersApps, deployDates:DeployDates]
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    String name
    String urlRepository
    String status = "pendiente"
    String type
    String criticality
    Date dateCreated
    Date lastUpdated
    Date dateUndeploy
    String description
    
    static mapping ={
        type sqlType:"Enum('frontend','backend','aplication','base de datos')"
        criticality sqlType: "Enum('indiferente', 'baja', 'media', 'alta', 'critica')"
        status sqlType : "Enum('activa','deprecada','pendiente','desarollo')"
        version false

    }   
    static constraints = {
        urlRepository nullable:true, maxSize:150, blank:true
        type inList:['frontend','backend','aplication','base de datos']
        dateUndeploy nullable:true, blank:true
        status inList: ['activa','deprecada','pendiente','desarollo']
        criticality inList: ["indiferente", "baja", "media", "alta", "critica"], blank: true, nullable:true
        uuid maxSize:32, unique:true
        description maxSize:150
        name maxSize:50 
    }
}

class DeployDates{
    Date dateDeploy = new Date()
    String typeEnvironment
    Apps app
    String version
    
    static mapping = {
        version false
        typeEnvironment sqlType: "Enum('Pruebas', 'Desarrollo', 'Produccion')"
    }
    
    static constraints = {
        typeEnvironment inList:['Pruebas', 'Desarrollo', 'Produccion']
    }
}

class AppConnections{
    Apps app
    Apps service
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    String description
    String portApp
    String portService
    Date dateCreated
    Date lastUpdated
    static mapping = {
        version false
    }
    static constraints ={
        uuid unique:true, maxSize:32
        description maxSize:150, nullable:true, blank:true
        portApp maxSize:5, nullable:true, blank:true
        portService maxSize:5, nullable:true, blank:true
    }
    
    
}