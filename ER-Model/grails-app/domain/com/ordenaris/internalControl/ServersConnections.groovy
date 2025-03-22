package com.ordenaris.internalControl

class ServersConnections {
    String portServer
    String portSubServer
    String status
    Date dateCreated
    Date lastUpdated
    
    static belongsTo = [server: Servers, subServer:Servers]

    static mapping = {
        portServer index:"serverConnections_portServer_idx"
        portSubServer index:"serverConnections_portSubServer_idc"
        status index:"serverConnections_status_idx"
        dateCreated index:"serverConnections_dateCreated_idx"
        lastUpdated index:"serverConnections_lastUpdated_idx"
        version false
    }
    static constraints = {
        server nullable: false
        subServer nullable: false
        portServer unique: true,maxSize: 5
        portSubServer unique: true,maxSize: 5
        lastUpdated blank: true, nullable: true
        status inList:[Constants.STATUS_ACTIVE,Constants.STATUS_INACTIVE,Constants.STATUS_UPKEEP,Constants.STATUS_TESTS]
    }
}