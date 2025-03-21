package com.ordenaris.internalControl

class PositionEmployees {
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    Date dateCreated
    Date lastUpdated
    String name
    String description
    String area
    
    static hasmany=[employee:Employees]
    
    static mapping = {
        uuid index:"positionEmployees_uuid_idx"
        dateCreated index:"positionEmployees_dateCreated_idx"
        lastUpdated index:"positionEmployees_lastUpdated_idx"
        name index:"positionEmployees_name_idx"
        description index:"positionEmployees_description_idx"
        area index:"positionEmployees_description_idx"
        version false
    }

    static constraints = {
        uuid maxSize:32, unique:true
        name maxSize:50, unique:true
        description maxSize:150
        area maxSize:30
    }
}
