package com.ordenaris.internalControl

class Enterprises {
    String uuid = UUID.randomUUID().toString().replaceAll('\\-','')
    String name
    String type
    String description
    Date dateCreated
    Date lastUpdated
    
    static hasmany = [dev:Devs, server: Servers]
    
    static mapping = {
        uuid index:"enterprises_uuid_idx"
        name index:"enterprises_name_idx"
        type index:"enterprises_type_idx"
        description index:"enterprises_name_idx"
        dateCreated index:"enterprises_dateCreated_idx"
        lastUpdated index:"enterprises_lastUpdated_idx"
        version false
    }
    static constraints = {
        name maxSize:50
        uuid maxSize:32, unique:true
        type maxSize:20, inList:[Constants.TYPE_ENTERPRICE_CLIENT,Constants.TYPE_ENTERPRICE_INTERNAL]
        description maxSize:150
    }
}