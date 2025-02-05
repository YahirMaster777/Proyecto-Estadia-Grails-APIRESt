package com.ordenaris.internalControl
import java.util.UUID

class Programs_apps {
    static hasMany = [servers:Servers]
    static belongsTo = Servers
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')

    String software
    String version
    String description
    int status
    String ports
    static constraints = {
        uuid unique: true
    }
    static mapping = {
        version false
    }
}
