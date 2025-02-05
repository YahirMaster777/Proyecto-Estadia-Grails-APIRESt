package com.ordenaris.internalControl
import java.util.UUID

class Customers {
    static hasMany =[app: App]
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    
    String name
    int status
    static constraints = {
        uuid unique: true
    }
    static mapping = {
        version false
    }
}
