package com.ordenaris.internalControl

class Profile {
    String uuid = UUID.randomUUID().toString().replaceAll('//-','')
    Date dateCreated
    Date lastUpdated
    Integer status = 1
    String name
    Section section
    Permission permission
    
    static mapping = {
        version false
    }
    
    
    static constraints = {
        uuid unique:true, maxSize:32
        name maxSize:40
    }
}
