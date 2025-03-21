package com.ordenaris.internalControl

import java.util.UUID

class ServersApps {

    static belongsTo = [server: Servers, app: Apps, locationConfig:Configs]
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    String description
    String portApp
    String portServer
    String status = Constants.STATUS_ACTIVE
    Date dateCreated
    Date lastUpdated
    String environmentType
    Date dateDeploy
    

    static mapping = {
        uuid index:"serverApps_uuid_idx"
        description index:"serverApps_description_idx"
        portApp index:"serverApps_portApp_idx"
        portServer index:"serverApps_portServer_idx"
        status index:"serverApps_status_idx"
        dateCreated index:"serverApps_dateCreated_idx"
        lastUpdated index:"serverApps_lastUpdated_idx"
        environmentType index:"serverApps_environmentType_idx"
        dateDeploy index:"serverApps_dateDeploy_idx"
        version false

    }
    static constraints = {
        uuid unique:true, maxSize:32
        portApp unique: true,maxSize: 5
        portServer unique: true,maxSize: 5
        description blank:true, nullable:true, maxSize:150
        lastUpdated blank: true, nullable: true
        environmentType inList:[Constants.STATUS_TESTS,Constants.STATUS_PRODUCTION,Constants.STATUS_DEVELOPMENT]
        status inList:[Constants.STATUS_ACTIVE,Constants.STATUS_INACTIVE,Constants.STATUS_UPKEEP,Constants.STATUS_TESTS], blank: true, nullable:true
    }
}
