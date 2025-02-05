package com.ordenaris.internalControl

class Section {
    Date date_created = new Date()
    String uuid = UUID.randomUUID().toString()
    Date lastUpdated
    String url 
    String name
    boolean status
    Integer type
    

    static mapping = {
        status sqlType: "bit"
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
