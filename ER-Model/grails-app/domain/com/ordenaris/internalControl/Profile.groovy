package com.ordenaris.internalControl

class Profile {
    String uuid = UUID.randomUUID().toString().replaceAll('//-','')
    Date dateCreated
    Date lastUpdated
    Integer status = 1
    String name
    static mapping = {
        version false
    }
    
    
    static constraints = {
        uuid unique:true, maxSize:32
    }
}
