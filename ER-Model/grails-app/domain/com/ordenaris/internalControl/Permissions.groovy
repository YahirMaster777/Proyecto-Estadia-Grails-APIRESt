package com.ordenaris.internalControl

class Permissions {
    Date dateCreated
    String name
    String description
    String alias
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    static hasmany = [templates: TemplatePermissions]
    static belongsTo = [section: Sections]
    
    static mapping ={
        dateCreated index: "permissions_dateCreated_idx"
        name index: "permissions_name_idx"
        description index: "permissions_description_idx"
        alias index: "permissions_alias_idx"
        uuid index: "permissions_uuid_idx"
        version false
    }
    static constraints = {
        uuid unique:true, maxSize:32
        name maxSize:50
        description maxSize:150
        alias maxSize:50, unique: true
    }
}
