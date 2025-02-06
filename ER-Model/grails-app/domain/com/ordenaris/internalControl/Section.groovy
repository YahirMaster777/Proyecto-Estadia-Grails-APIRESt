package com.ordenaris.internalControl

class Section {
    Date date_created 
    String uuid = UUID.randomUUID().toString().replaceAll('//-', '')
    Date lastUpdated
    String url
    String name
    Integer status =1 
    Integer type
    
    static mapping = {
        version false
    }
    static constraints = {
        lastUpdated nullable:true, blank:true
        uuid nullable: false, blank: false, unique: true, maxSize:32
        url nullable: false, blank: false, maxSize:45
        name nullable: false, blank: false, maxSize:150
        status nullable: false
        type nullable: false, blank: false, maxSize: 11
    }
}
