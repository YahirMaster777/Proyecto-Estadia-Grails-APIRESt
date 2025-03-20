package com.ordenaris.internalControl
import java.util.UUID
class Backups {
    Servers server
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    String description
    Date dateCreated
    String type
    String url
    static mapping = {
        version false
        type sqlType:"Enum('local','nube','servidor')"
    }
    static constraints = {
        uuid unique: true,maxSize:32
        type inList:['local','nube','servidor']
        url nullable: false, blank: false
        description maxSize:150, blank:true, nullable: true
    }
}
