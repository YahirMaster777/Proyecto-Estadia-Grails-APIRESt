package com.ordenaris.internalControl

class Enterprises {
    String uuid = UUID.randomUUID().toString().replaceAll('//-','')
    String name
    String type
    String description
    Date dateCreated
    Date lastUpdated
    
    static mapping = {
        version false
    }
    static constraints = {
        name maxSize:40
        uuid maxSize:32, unique:true
        type maxSize:20, inList:['Cliente', 'Interna']
        description maxSize:150
    }
}
