package com.ordenaris.internalControl
import java.util.UUID
class Servers {
    static hasMany =[connectionServer: ConnectionsServers, backup:Backups, serverApp: ServersApps]
    ConnectionsServers connectionServer
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')

    String host
    String publicIp
    String privateIp
    String capacity
    String memory
    String storage
    String cloudProvider
    String locationConfig
    String type
    Employees responsible
    int status = 1
    Enterprises company
    String macAddress
    int criticaly
    String development
    Date dateCreated
    Date lastUpdated

    static constraints = {
        uuid unique: true, maxSize: 32
        development inList: ["pruebas", "producción", "desarrollo"], maxSize: 10
        type inList: ["virtual", "fisico", "dedicado"], maxSize:10
        publicIp blank:true, nullable:true,maxSize: 15
        privateIp blank:true, nullable: true,maxSize: 15
        lastUpdated blank:true, nullable: true
        macAddress unique:true,maxSize: 17
        host maxSize: 20
        cloudProvider: 50
        capacity: 7
        memory: 7
    }
    static mapping = {
        version false
    }
}