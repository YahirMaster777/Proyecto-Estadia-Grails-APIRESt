package com.ordenaris.internalControl

class App {
    static hasMany = [serverApp: ServerApp]
    
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
    Date dateSwitchOff
    String locationConfig
    String description
    
    static mapping ={
        version false
    }
    static constraints = {
        type inList:['Frontend','Backend','App','DB']
        dateSwitchOff nullable:true
        domain nullable:true, maxSize:150
        criticality nullable:true,maxSize:11
        locationConfig nullable:true
        versionApp nullable:true
        uuid maxSize:32, unique:true
        port blank:true, nullable:true,maxSize:15
        host blank:true, nullable:true,maxSize:20
    }
}

class ConnectionApp{
    App app
    App service
    String description
    String status
    String portApp
    String portService
    Date dateCreated
    Date lastUpdated
    
    static mapping = {
        version false
    }
    
    static constraints ={
        description maxSize:100, nullable:true, blank:true
        status maxSize:20
        portApp maxSize:5, nullable:true, blank:true
        portService maxSize:5, nullable:true, blank:true
    }
}
