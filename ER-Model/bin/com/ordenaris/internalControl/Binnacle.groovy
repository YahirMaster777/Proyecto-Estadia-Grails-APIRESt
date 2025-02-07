package com.ordenaris.internalControl
import java.util.UUID

class Binnacle {
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    Date registration
    String description
    User user
    String tableName
    String extra

    static constraints = {
        uuid unique: true
        extra blank:true, nullable:true
        table inList: ["App", "Servidor"]
    }
    
    static mapping = {
        version false
    }
}
