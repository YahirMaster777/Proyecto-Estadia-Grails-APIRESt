package com.ordenaris.internalControl
import java.util.UUID

class Binnacle {
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
        table inList: ["Apps", "Servidor"]
    }
    
    static mapping = {
        version false
    }
}
