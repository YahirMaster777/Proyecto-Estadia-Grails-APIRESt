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
        type sqlType:"Enum('virtual','fisico','dedicado')"
        criticality sqlType:"Enum('indiferente','baja','media','alta','critica')"
        status sqlType:"Enum('activo','inactivo','mantenimiento')"
    }
    static constraints = {
        uuid unique: true, maxSize: 32
        type inList:['virtual','fisico','dedicado']
        criticality inList:['indiferente','baja','media','alta','critica'], blank: true, nullable:true
        status :['activo','inactivo','mantenimiento']
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