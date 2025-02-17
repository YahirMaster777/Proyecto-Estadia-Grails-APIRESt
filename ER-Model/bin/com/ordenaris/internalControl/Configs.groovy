package com.ordenaris.internalControl
import java.util.UUID

class Configs {
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')

    String path
    Users responsible
    String name
    Date dateCreated
    Date lastUpdated
    String type

    static mapping = {
        version false
        type sqlType:"Enum('Programa','Servcio','Aplicación','Base de datos','Servidor','Respaldo')"
    }

    static constraints = {
        uuid unique:true, maxSize:32
        lastUpdated nullable: true, blank:true
        name maxSize: 50
        type inList: ['Programa','Servcio','Aplicación','Base de datos','Servidor','Respaldo']
    }
}
