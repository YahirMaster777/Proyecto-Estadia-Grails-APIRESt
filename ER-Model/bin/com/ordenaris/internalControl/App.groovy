package com.ordenaris.internalControl

class App {
    static hasMany = [server:Server]
    static belongsTo = Server
    String uuid = UUID.randomUUID().toString().replaceAll('//-', '')
    String name 
    Integer status = 1
    String versionApp
    String port
    String host
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
        criticality nullable:true,maxSize:11
        locationConfig nullable:true
        versionApp nullable:true
        domain nullable:true, maxSize:150
        criticality nullable:true,maxSize:11
        uuid maxSize:32, unique:true
        port blank:true, nullable:true,maxSize:15
        host blank:true, nullable:true,maxSize:20


        
    }
}


class ConnectionApp{
    App app
    App service
    String description
    
    static mapping = {
        version false
    }
    
    static constraints ={
    }
}
