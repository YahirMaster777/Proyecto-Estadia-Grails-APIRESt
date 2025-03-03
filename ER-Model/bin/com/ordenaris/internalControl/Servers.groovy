package com.ordenaris.internalControl
import java.util.UUID
class Servers {
    static hasMany =[ backup:Backups, serverApp: ServersApps]
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')

    String host
    String publicIp
    String privateIp
    String processingCapacity
    String memory
    String storage
    String cloudProvider
    String type
    Enterprises company
    String macAddress
    String criticality
    String location
    String status
    Date dateCreated
    Date lastUpdated
    Date dateDeploy
    Date dateLastDeploy
    Date dateAcquisition
    String operatingSystem

    static mapping = {
        version false
        type sqlType:"Enum('Virtual','Fisico','Dedicado')"
        criticality sqlType:"Enum('Indiferente','Baja','Media','Alta','Critica')"
        status sqlType:"Enum('Activo','Inactivo','Mantenimiento')"
    }
    static constraints = {
        uuid unique: true, maxSize: 32
        type inList:['Virtual','Fisico','Dedicado']
        criticality inList:['Indiferente','Baja','Media','Alta','Critica'], blank: true, nullable:true
        status :['Activo','Inactivo','Mantenimiento']
        publicIp blank:true, nullable:true,maxSize: 15
        privateIp blank:true, nullable: true,maxSize: 15
        lastUpdated blank:true, nullable: true
        dateLastDeploy blank:true, nullable:true
        macAddress unique:true,maxSize: 17
        location maxSize: 50
        company blank:true, nullable: true
        host maxSize: 20
        cloudProvider maxSize: 20
        processingCapacity maxSize: 7
        storage maxSize: 7
        operatingSystem maxSize:20
        memory maxSize: 7
    }
}