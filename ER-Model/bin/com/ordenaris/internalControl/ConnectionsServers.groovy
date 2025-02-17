package com.ordenaris.internalControl

class ConnectionsServers {
    Servers server
    Servers subServer
    String description
    String portServer
    String portSubServer
    String status = 'Activo'
    Date dateCreated
    Date lastUpdated

    static constraints = {
        server nullable: false
        subServer nullable: false
        portServer unique: true,maxSize: 5
        portSubServer unique: true,maxSize: 5
        description blank:true, nullable:true, maxSize:150
        lastUpdated blank: true, nullable: true
        status inList:['Activo','Inactivo','Desabilitado']
    }

    static mapping = {
        version false
        status sqlType:"Enum('Activo','Inactivo','Desabilitado')"

    }
}
