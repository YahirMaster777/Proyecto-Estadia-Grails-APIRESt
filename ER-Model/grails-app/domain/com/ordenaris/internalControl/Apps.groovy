package com.ordenaris.internalControl

class Apps {
    static hasMany = [serverApp: ServersApps, deployDates:DeployDates, dev:Devs]
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    String name
    String urlRepository
    String status = Constants.STATUS_PENDING
    String type
    String criticality
    Date dateCreated
    Date lastUpdated
    Date dateUndeploy
    String description
    
    static mapping ={
        uuid index:"apps_uuid_idx"
        name index:"apps_name_idx"
        urlRepository index:"apps_urlRepository_idx"
        dateCreated index:"apps_dateCreates_idx"
        lastUpdated index:"apps_lastUpdated_idx"
        dateUndeploy index:"apps_dateUndeploy_idx"
        description index:"apps_description_idx"
        type index:"apps_type_idx"
        criticality index:"apps_criticality_idx"
        status index:"apps_status_idx"
        version false

    }   
    
    static constraints = {
        urlRepository nullable:true, maxSize:150, blank:true
        type inList:[Constants.TYPE_APP_BACKEND,Constants.TYPE_APP_APPLICATION, Constants.TYPE_APP_DATABASE, Constants.TYPE_APP_FRONTEND]
        dateUndeploy nullable:true, blank:true
        status inList: [Constants.STATUS_ACTIVE,Constants.STATUS_DEPRECATED,Constants.STATUS_PENDING,Constants.STATUS_DEVELOPMENT]
        criticality inList: [Constants.CRITICALITY_INDIFFERENT,Constants.CRITICALITY_LOW,Constants.CRITICALITY_MID,Constants.CRITICALITY_HIGH,Constants.CRITICALITY_CRITICIZES], blank: true, nullable:true
        uuid maxSize:32, unique:true
        description maxSize:150
        name maxSize:50 
    }
}


