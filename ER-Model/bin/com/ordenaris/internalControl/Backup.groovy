package com.ordenaris.internalControl
import java.util.UUID
class Backups {
    Servers server
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    String locationConfig
    String description
    int status = 1
    Date dateCreated
    Date lastUpdated
    //en formato {local, nube o servidor}, ruta
    String url

    static constraints = {
        uuid unique: true,maxSize:32
        lastUpdated blank: true, nullable: true
        description maxSize:150, blank:true, nullable: true
        url nullable: false, blank: false
    }
    static mapping = {
        version false
    }
}
