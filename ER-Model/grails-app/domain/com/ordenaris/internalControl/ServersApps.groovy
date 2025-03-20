package com.ordenaris.internalControl
import java.util.UUID

class ServersApps {
    static belongsTo = [server: Servers, app: Apps]
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')

    Configs locationConfig
    Servers server
    String description
    String portApp
    String portServer
    String status = 'activa'
    Date dateCreated
    Date lastUpdated
    String environmentType
    Date dateDeploy

    static mapping = {
        version false
        status sqlType:"Enum('activa','Inactiva','Matenimiento','Pruebas')"
        environmentType sqlType:"Enum('Pruebas','Producción','Desarrollo')"

    }
    static constraints = {
        uuid unique:true, maxSize:32
        portApp unique: true,maxSize: 5
        portServer unique: true,maxSize: 5
        description blank:true, nullable:true, maxSize:150
        lastUpdated blank: true, nullable: true
        environmentType inList:['Pruebas','Producción','Desarrollo']
        status inList:['activa','Inactiva','Matenimiento','Pruebas'], blank: true, nullable:true
    }
}
