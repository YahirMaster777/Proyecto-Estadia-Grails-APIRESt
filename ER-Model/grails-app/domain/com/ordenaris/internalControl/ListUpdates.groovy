package com.ordenaris.internalControl

class ListUpdates {
    Date dateLastDeploy
    String description
    
    static belongsTo = [ conection: ServersApps]
    static constraints = {
        description maxSize:150
    }
    static mapping = {
        dateLastDeploy index:"listUpdates_dateLastDeploy_idx"
        description index:"listUpdates_description_idx"
        version false
    }
}
