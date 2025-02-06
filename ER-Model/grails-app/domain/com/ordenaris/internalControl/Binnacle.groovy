package com.ordenaris.internalControl
import java.util.UUID

class Binnacle {
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    
    Date regisUpdate
    String description
    String user
    String table
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
