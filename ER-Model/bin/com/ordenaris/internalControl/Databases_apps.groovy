package com.ordenaris.internalControl
import java.util.UUID

class Databases_apps {
    static hasMany = [api: Api]
    static belongsTo = [server: Servers]
    
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    String name
    String managment
    String type
    String publicIp
    String privateIp
    
    static constraints = {
        uuid unique: true
    }
    static mapping = {
        version false
    }
}
