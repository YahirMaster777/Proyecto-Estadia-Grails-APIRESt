package com.ordenaris.internalControl

class UserSectionPermission {   
    Users user
    Permissions permission
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    
    static constraints = {
        uuid maxSize:32, unique:true
    }
    
    static mapping = {
        version false
    }
}
