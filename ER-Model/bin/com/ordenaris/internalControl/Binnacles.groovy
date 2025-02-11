package com.ordenaris.internalControl
import java.util.UUID

class Binnacles {
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    Date dateCreated
    String description
    Users user
    String tableName
    String extra
    String url
    int status = 1

    static constraints = {
        uuid unique: true,maxSize: 32
        extra blank:true, nullable:true
        tableName maxSize: 20
        url nullable: false, blank: false
        table inList: ["Apps", "Servidor"]
        description maxSize:150, blank:true, nullable: true
    }
    
    static mapping = {
        version false
    }
}
