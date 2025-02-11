package com.ordenaris.internalControl

class Apps {
    static hasMany = [serverApp: ServersApps]
    
    String uuid = UUID.randomUUID().toString().replaceAll('//-', '')
    String name 
    Integer status = 1
    String versionApp
    String port
    String host
    String ipAdress
    String domain
    String type
    Integer criticality
    Date dateCreated
    Date lastUpdated
    Date dateDeploy
    Date dateUndeploy
    String locationConfig
    String description
    
    static mapping ={
        version false
    }
    static constraints = {
        type inList:['Frontend','Backend','Apps','DB']
        dateUndeploy nullable:true, blank:true
        dateDeploy nullable:true,blank:true
        domain nullable:true, maxSize:150
        criticality nullable:true,maxSize:11
        locationConfig nullable:true
        versionApp nullable:true, maxSize:20
        uuid maxSize:32, unique:true
        port blank:true, nullable:true,maxSize:5
        host blank:true, nullable:true,maxSize:20
        ipAdress maxSize:15
        description maxSize:150
        name maxSize:40
    }
}

class ConnectionApp{
    Apps app
    Apps service
    String description
    String status =1 
    String portApp
    String portService
    Date dateCreated
    Date lastUpdated
    
    static mapping = {
        version false
    }
    
    static constraints ={
        description maxSize:150, nullable:true, blank:true
        portApp maxSize:5, nullable:true, blank:true
        portService maxSize:5, nullable:true, blank:true
    }
}
