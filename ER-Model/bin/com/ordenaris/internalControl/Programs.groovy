package com.ordenaris.internalControl
import java.util.UUID

class Programs {
    // almacena los programas y los servicios
    static hasMany = [server:Servers]
    Servers server
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')

    String software
    String version
    // se metete el balanceador
    String description 
    int status = 1
    String port
    String locationConfig
    String type
    Date instalation
    Date dateCreated
    Date lastUpdated

    static constraints = {
        uuid unique: true, maxSize: 32
        port unique: true, maxSize: 5
        type inList: ["programa", "servicio"],maxSize: 10
        description blank:true, nullable:true, maxSize:150
        instalation blank:true, nullable: true
        lastUpdated blank:true, nullable: true
        software maxSize: 30
    }
    static mapping = {
        version false
    }
}
