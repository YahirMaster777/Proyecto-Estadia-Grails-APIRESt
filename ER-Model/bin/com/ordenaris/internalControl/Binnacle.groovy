package com.ordenaris.internalControl
import java.util.UUID

class Binnacles {
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    Date dateCreated
    String description
    User user
    String tableName
    String extra
    String url
    int status = 1

    static constraints = {
        uuid unique: true,maxSize: 32
        extra blank:true, nullable:true
        tableName maxSize: 20
        table inList: ["App", "Servidor"]
        url nullable: false, blank: false, maxSize:45
    }
    
    static mapping = {
        version false
    }
}
