package com.ordenaris.internalControl
import java.util.UUID

class Servers {
    static hasMany = [programs_apps:Programs_apps, services_server: Services_server, backup_configuration:Backup_configuration]
    static belongsTo = [server: Servers]

    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    String hostname
    String publicIp
    String privateIp
    String capacity
    String memory
    String storage
    String cloudProvider
    String location
    String type
    String responsible
    int status
    String company
    String criticality
    String QA
    static constraints = {
        uuid unique: true
    }
    static mapping = {
        version false
    }
}
