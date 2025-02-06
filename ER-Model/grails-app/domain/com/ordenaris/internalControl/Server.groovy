package com.ordenaris.internalControl
import java.util.UUID
class Server {
    static hasMany =[serverServer: ServerServer, backup:Backup]
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
    String criticaly
    App app
    String development

    static constraints = {
        uuid unique: true
        development inList: ["pruebas", "producción", "desarrollo"]
        type inList: ["virtual", "fisico", "dedicado"]
        company inList: ["Innovattia", "Pawerful", "Ordenaris"]
        criticaly inList: ["baja", "media", "alta"]
        publicIp blank:true, nullable:true
        privateIp blank:true, nullable: true
    }
    static mapping = {
        version false
    }
}
