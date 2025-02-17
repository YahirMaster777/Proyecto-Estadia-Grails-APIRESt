package com.ordenaris.internalControl
import java.util.UUID

class ServersApps {
    static belongsTo = [server: Servers, app: Apps]
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')

    Servers server
    String description
    String portApp
    String portServer
    String status
    Date dateCreated
    Date lastUpdated

    static mapping = {
        version false
        status sqlType:"Enum('Activa','Inactiva','Matenimiento','Pruebas')"
    }
    static constraints = {
        uuid unique:true, maxSize:32
        portApp unique: true,maxSize: 5
        portServer unique: true,maxSize: 5
        description blank:true, nullable:true, maxSize:150
        lastUpdated blank: true, nullable: true
        status inList:['Activa','Inactiva','Matenimiento','Pruebas']
    }
}
