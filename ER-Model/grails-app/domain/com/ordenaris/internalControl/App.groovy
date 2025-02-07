package com.ordenaris.internalControl

class App {
    static hasMany = [serverApp: ServerApp]
    
    String uuid = UUID.randomUUID().toString().replaceAll('//-', '')
    String name 
    Integer status = 1
    String version
    String port
    String host
    String domain
    String type
    Integer criticality
    Date dateCreated
    Date lastUpdated
    String locationConfig
    String description
    
    static mapping ={
        version false
    }
    static constraints = {
        type inList:['Frontend','Backend','App','DB']
        domain nullable:true, maxSize:150
        criticality nullable:true,maxSize:11
        locationConfig nullable:true
        version nullable:true
        uuid maxSize:32, unique:true
        port blank:true, nullable:true,maxSize:15
        host blank:true, nullable:true,maxSize:20


        
    }
}

class UseApp{
    App app
    App service
    String description
    
    static mapping = {
        version false
    }
    
    static constraints ={
    }
}
