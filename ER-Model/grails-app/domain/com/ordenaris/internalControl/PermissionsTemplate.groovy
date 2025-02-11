package com.ordenaris.internalControl

class PermissionsTemplate {
    String uuid = UUID.randomUUID().toString().replaceAll('//-','')
    Date dateCreated
    Date lastUpdated
    Integer status = 1
    String name
    Sections section
    Permissions permission
    
    static mapping = {
        version false
    }
    
    
    static constraints = {
        uuid unique:true, maxSize:32
        name maxSize:50
    }
}
