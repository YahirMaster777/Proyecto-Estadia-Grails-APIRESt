package com.ordenaris.internalControl

class UserSectionPermission {   
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    
    static belongsTo = [ user:Users, permission:Permissions]
    
    static constraints = {
        uuid maxSize:32, unique:true
    }
    
    static mapping = {
        uuid index:"userPermission_uuid_idx"
        version false
    }
}
