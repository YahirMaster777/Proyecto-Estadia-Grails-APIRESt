package com.ordenaris.internalControl
import java.util.UUID

class Program {
    // almacena los programas y los servicios
    static hasMany = [server:Server]
    Server server
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')

    String software
    String version
    // se metete el balanceador
    String description 
    int status
    String port
    String locationConfig
    String type
    Date instalation
    Date dateCreated
    Date lastUpdated

    static constraints = {
        uuid unique: true
        type inList: ["programa", "servicio"]
        description blank:true, nullable:true
        instalation blank:true, nullable: true
        lastUpdated blank:true, nullable: true
    }
    static mapping = {
        version false
    }
}
