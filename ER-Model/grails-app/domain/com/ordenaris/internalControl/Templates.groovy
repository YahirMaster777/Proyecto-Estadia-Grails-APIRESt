package com.ordenaris.internalControl

class Templates {
    String uuid = UUID.randomUUID().toString().replaceAll('\\-','')
    Date dateCreated
    Date lastUpdated
    String name
    String status = Constants.STATUS_ACTIVE
    String description
    
    static hasMany=[permissions:TemplatePermissions]
    
    static mapping = {
        uuid index:"templates_uuid_idx"
        dateCreated index:"templates_dateCreated_idx"
        lastUpdated index:"templates_lastUpdated_idx"
        name index:"templates_name_idx"
        status index:"templates_status_idx"
        description index:"templates_description_idx"
        version false
    }
    
    static constraints = {
        status inList:[Constants.STATUS_ACTIVE,Constants.STATUS_INACTIVE]
        uuid unique:true, maxSize:32
        name maxSize:50, unique:true
        description maxSize:150
    }
    
}