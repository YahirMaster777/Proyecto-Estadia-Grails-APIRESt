package com.ordenaris.internalControl
import java.util.UUID

class Programs {
    // almacena los programas y los servicios
    static hasMany = [server:Servers]
    Servers server
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')

    String software
    String version
    String description   // se metete el balanceador
    int status = 1 //1-acntivo ,2-innactivo, 3-, 4-, 5-, 6-, 7- 
    String port
    Config locationConfig
    String type
    Date instalation
    Date dateCreated
    Date lastUpdated

    static constraints = {
        uuid unique: true, maxSize: 32
        port unique: true, maxSize: 5
        type inList: ["Programa", "Servicio"]
        description blank:true, nullable:true, maxSize:150
        instalation blank:true, nullable: true
        lastUpdated blank:true, nullable: true
        software maxSize: 30
        version maxSize:10
    }
    static mapping = {
        version false
    }
}
