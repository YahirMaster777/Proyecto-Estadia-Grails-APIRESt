package com.ordenaris.internalControl

class Enterprises {
    String uuid = UUID.randomUUID().toString().replaceAll('\\-','')
    String name
    String type
    String description
    Date dateCreated
    Date lastUpdated
    
    static mapping = {
        version false
        type sqlType:"Enum('cliente', 'interna')"
    }
    static constraints = {
        name maxSize:50
        uuid maxSize:32, unique:true
        type maxSize:20, inList:['cliente', 'interna']
        description maxSize:150
    }
}
