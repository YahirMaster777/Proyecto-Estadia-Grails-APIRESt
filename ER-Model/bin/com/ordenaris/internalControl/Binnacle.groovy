package com.ordenaris.internalControl
import java.util.UUID

class Binnacle {
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    Date dateCreated
    String description
    User user
    String tableName
    String extra
    String url

    static constraints = {
        uuid unique: true,maxSize: 32
        extra blank:true, nullable:true
        table inList: ["App", "Servidor"]
    }
    
    static mapping = {
        version false
    }
}
