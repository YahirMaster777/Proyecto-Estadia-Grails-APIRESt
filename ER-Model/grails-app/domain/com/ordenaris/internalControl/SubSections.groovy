package com.ordenaris.internalControl

class SubSections{
    Date dateCreated
    Date lastUpdated
    String uuid =  UUID.randomUUID().toString().replaceAll('\\-', '')

    
    static belongsTo = [section:Sections, subSection:Sections]
    
    static mapping = {
        uuid index:"subSections_uuid_idx"
        dateCreated index:"subSections_dateCreated_idx"
        lastUpdated index:"subSections_lastUpdated_idx"
        version false
    }
    
    static constraints = {
        uuid unique: true, maxSize:32
        url nullable: false, blank: false
    }
}