package com.ordenaris.internalControl
import java.util.UUID
class Backups {
    Servers server
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    String description
    String status
    Date dateCreated
    String type
    String url

    static constraints = {
        uuid unique: true,maxSize:32
        type inList:["Local", "Nube", "Servidor"]
        url nullable: false, blank: false
        description maxSize:150, blank:true, nullable: true
        status inList:["Completo", "Pausado", "Renaudado", "Cancelado"]
    }
    static mapping = {
        version false
    }
}
