package com.ordenaris.internalControl
import java.util.UUID
class Server {
    static hasMany =[serverServer: ServerServer, backup:Backup, serverApp: ServerApp]
    ServerServer serverServer
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
    String responsible
    int status
    String company
    int criticaly
    String development
    Date dateCreated
    Date lastUpdated

    static constraints = {
        uuid unique: true
        development inList: ["pruebas", "producción", "desarrollo"]
        type inList: ["virtual", "fisico", "dedicado"]
        company inList: ["Innovattia", "Pawerful", "Ordenaris"]
        publicIp blank:true, nullable:true
        privateIp blank:true, nullable: true
        lastUpdated blank:true, nullable: true
    }
    static mapping = {
        version false
    }
}
