package com.ordenaris.internalControl

class TemplatePermissions {
    Date dateCreated
    Date lastUpdated
    
    static belongsTo= [permission:Permissions, template:Templates]
    
    
    
    static mapping = {
        dateCreated index:"templatePermissions_dateCreated_idx"
        lastUpdated index:"templatePermissions_lastUpdated_idx"
        version false
        
    }
}

