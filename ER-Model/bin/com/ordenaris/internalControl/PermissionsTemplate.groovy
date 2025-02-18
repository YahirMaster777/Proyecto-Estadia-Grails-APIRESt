package com.ordenaris.internalControl

class TemplatePermissions {
    Date dateCreated
    Date lastUpdated
    Sections section
    String description
    Permissions permission
    Templates template
    
    static mapping = {
        version false
    }
    
    
    static constraints = {
        uuid unique:true, maxSize:32
        name maxSize:50
        description maxSize:150
    }
}

class Templates {
    String uuid = UUID.randomUUID().toString().replaceAll('\\-','')
    Date dateCreated
    Date lastUpdated
    String name
    String description
    
    static mapping = {
        version false
    }
    static constraints = {
        uuid unique:true, maxSize:32
        name maxSize:50
        description maxSize:150
    }
    
}
