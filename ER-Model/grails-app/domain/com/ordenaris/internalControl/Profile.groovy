package com.ordenaris.internalControl

class Profile {
    String uuid = UUID.randomUUID().toString().replaceAll('//-','')
    Date dateCreated
    Date lastUpdate
    Integer status = 1
    String name
    Section sectionProfile
    static mapping = {
        version false
    }
    
    
    static constraints = {
        uuid unique:true, maxSize:32
    }
}
