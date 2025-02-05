package com.ordenaris.internalControl
import java.util.UUID

class Backup_configuration {
    static hasMany = [servers:Servers]
    static belongsTo = Servers
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    String location
    String backed
    int status
    String route

    static constraints = {
        uuid unique: true
    }
    static mapping = {
        version false
    }
}
