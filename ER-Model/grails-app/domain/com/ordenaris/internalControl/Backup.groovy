package com.ordenaris.internalControl
import java.util.UUID
class Backup {
    Server server
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    String locationConfig
    String description
    int status
    Date dateCreated
    Date lastUpdated
    //en formato {local, nube o servidor}, ruta
    String url

    static constraints = {
        uuid unique: true
        lastUpdated blank: true, nullable: true
    }
    static mapping = {
        version false
    }
}
