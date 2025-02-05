package com.ordenaris.internalControl

class Api {
    
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    String host 
    String port
    String criticality
    Integer status = 1
    Date dateCreated
    Date lastUpdate
    String version
    String name
    
    static hasMany=[apps:App]
    
    static mapping = {
        version false
    }
    
    
    
    static constraints = {
        criticality nullable:true, maxSize:60
        uuid maxSize:32
        
    }
}
