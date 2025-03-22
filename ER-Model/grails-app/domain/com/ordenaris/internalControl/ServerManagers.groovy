package com.ordenaris.internalControl

import java.util.UUID

class ServerManagers {
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    String incumbency
    Date assigned
    Date lastUpdated
    String statusAnalist
    
    static belongsTo = [responsible:Employees, server: Servers]
    
    static constraints = {
        uuid unique:true, maxSize:32
        statusAnalist inList:[Constants.STATUS_ACTIVE,Constants.STATUS_INACTIVE]
        incumbency inList:[Constants.INCUMBENCY_HIGH,Constants.INCUMBENCY_MID,Constants.INCUMBENCY_LOW]
    }
    static mapping = {
        uuid index:"serverManagers_uuid_idx"
        assigned index:"serverManagers_assigned_idx"
        lastUpdated index:"serverManagers_lastUpdated_idx"        
        statusAnalist index:"serverManagers_statusAnalist_idx"
        incumbency index:"serverManagers_incumbency_idx"
        version false
    }
}