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
    
    static belongsTo=[ company:Enterprises]
    
    static mapping = {
        uuid index:"servers_uuid_idx"
        host index: "servers_host_idx"
        publicIp index:"servers_publicIp_idx"
        privateIp index:"servers_privateIp_idx"
        processingCapacity index:"servers_processingCapacity_idx"
        memory index:"servers_memory_idx"
        storage index:"servers_storage_idx"
        cloudProvider index:"servers_cloudProvider_idx"
        type index: "servers_type_idx"
        macAddress index:"servers_macAddress_idx"
        criticality index:"servers_criticality_idx"
        location index:"severs_location_idx"
        status index:"servers_status_idx"
        dateCreated index:"servers_dateCreated_idx"
        lastUpdated index:"servers_lastUpdated_idx"
        dateDeploy index:"servers_dateDeploy_idx"
        dateLastDeploy index: "servers_dateLastDeploy_idx"
        dateAcquisition index: "servers_dateAcquisition_idx"
        operatingSystem index: "servers_operatingSystem_idx"
        version false
    }
    static constraints = {
        uuid unique: true, maxSize: 32
        type inList:[Constants.TYPE_SERVER_VIRTUAL,Constants.TYPE_SERVER_FISIC,Constants.TYPE_SERVER_DEDICATED]
        criticality inList:[Constants.CRITICALITY_INDIFFERENT,Constants.CRITICALITY_LOW,Constants.CRITICALITY_MID,Constants.CRITICALITY_HIGH,Constants.CRITICALITY_CRITICIZES], blank: true, nullable:true
        status inList :[Constants.STATUS_ACTIVE,Constants.STATUS_INACTIVE,Constants.STATUS_UPKEEP]
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