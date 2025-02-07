package com.ordenaris.internalControl
import java.util.UUID
class Server {
    static hasMany =[conexionServer: ConexionServer, backup:Backup, serverApp: ServerApp]
    ConexionServer conexionServer
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')

    String hostname
    String publicIp
    String privateIp
    String capacity
    String memory
    String storage
    String cloudProvider
    String locationConfig
    String type
    Employee responsible
    int status = 1
    Enterprise company
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
    }
    static mapping = {
        version false
    }
}
