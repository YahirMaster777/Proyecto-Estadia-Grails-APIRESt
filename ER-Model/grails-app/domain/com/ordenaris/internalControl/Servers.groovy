package com.ordenaris.internalControl
import java.util.UUID
class Servers {
    static hasMany =[ backup:Backups, serverApp: ServersApps]
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
    int status = 1
    Enterprises company
    String macAddress
    String criticality
    String development
    String location
    Date dateCreated
    Date lastUpdated

    static constraints = {
        uuid unique: true, maxSize: 32
        development inList: ["Pruebas", "Producción", "Desarrollo"]
        type inList: ["Virtual", "Fisico", "Dedicado"]
        criticality inList: ["Indiferente", "Baja", "Media", "Alta", "Critica"], blank: true, nullable:true
        publicIp blank:true, nullable:true,maxSize: 15
        privateIp blank:true, nullable: true,maxSize: 15
        lastUpdated blank:true, nullable: true
        macAddress unique:true,maxSize: 17
        location maxSize: 50
        company blank:true, nullable: true
        host maxSize: 20
        cloudProvider maxSize: 20
        capacity maxSize: 7
        storage maxSize: 7
        memory maxSize: 7
    }
    static mapping = {
        version false
    }
}