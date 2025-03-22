package com.ordenaris.internalControl

class AppConnections{
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    String description
    String portApp
    String portService
    Date dateCreated
    Date lastUpdated
    String status = Constants.STATUS_ACTIVE
    
    static belongsTo = [app : Apps, service: Apps]
    
    static mapping = {
        uuid index:'appConnections_uuid_idx'
        description index:'appConnections_description_idx'
        portApp index:'appConnections_portApp_idx'
        portService index:'appConnections_portService_idx'
        dateCreated index:'appConnections_dateCreated_idx'
        lastUpdated index:'appConnections_lastUpdated_idx'
        status index: "appConnections_status_idx"
        version false
    }
    static constraints ={
        status inList:[ Constants.STATUS_ACTIVE, Constants.STATUS_INACTIVE]
        uuid unique:true, maxSize:32
        description maxSize:150, nullable:true, blank:true
        portApp maxSize:5, nullable:true, blank:true
        portService maxSize:5, nullable:true, blank:true
    }
}