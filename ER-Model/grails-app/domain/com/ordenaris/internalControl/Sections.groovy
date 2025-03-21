package com.ordenaris.internalControl

class Sections {
    Date dateCreated 
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    Date lastUpdated
    String url
    String name
    String description
    String status = Constants.STATUS_ACTIVE
    
    static hasmany=[subSection:SubSections]
    
    static mapping = {
        dateCreated index:"sections_dateCreated_idx"
        uuid index:"sections_uuid_idx"
        lastUpdated index:"sections_lastUpdated_idx"
        url index:"sections_url_idx"
        name index:"sections_name_idx"
        description index:"sections_description_idx"
        status index:"sections_status_idx"
        version false
    }
    static constraints = {
        lastUpdated nullable:true, blank:true
        uuid nullable: false, blank: false, unique: true, maxSize:32
        url nullable: false, blank: false
        name nullable: false, blank: false, maxSize:50
        status nullable: false, inList:[Constants.STATUS_ACTIVE,Constants.STATUS_INACTIVE,Constants.STATUS_UPKEEP,Constants.STATUS_TESTS]
        description nullable:false, maxSize:150
    }
}
