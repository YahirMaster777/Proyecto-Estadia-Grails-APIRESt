package com.ordenaris.internalControl
import java.util.UUID

class Programs {
    // almacena los programas y los servicios
    Servers server
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')

    String software
    String version
    // se metete el balanceador
    String description    
    String port
    String type
    Date instalation
    Date dateCreated
    Date lastUpdated

    static mapping = {
        version false
        type sqlType:"Enum('Programa','Servicio')"
    }

    static constraints = {
        uuid unique: true, maxSize: 32
        port unique: true, maxSize: 5
        type inList:['Programa','Servicio']
        description blank:true, nullable:true, maxSize:150
        instalation blank:true, nullable: true
        lastUpdated blank:true, nullable: true
        software maxSize: 30
        version maxSize: 10
    }
}
