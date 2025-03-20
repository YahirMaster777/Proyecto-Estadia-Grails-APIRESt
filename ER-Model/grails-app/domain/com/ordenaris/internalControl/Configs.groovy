package com.ordenaris.internalControl
import java.util.UUID

class Configs {
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')

    String path
    String name
    Date dateCreated
    Date lastUpdated
    String type
    String description

    static mapping = {
        version false
        type sqlType:"Enum('programa','servicio','aplicacion','base de datos','servidor','respaldo')"
    }

    static constraints = {
        uuid unique:true, maxSize:32
        lastUpdated nullable: true, blank:true
        name maxSize: 50
        description maxSize:150, nullable:true, blank:true
        type inList: ['programa','servicio','aplicacion','base de datos','servidor','respaldo']
    }
}
