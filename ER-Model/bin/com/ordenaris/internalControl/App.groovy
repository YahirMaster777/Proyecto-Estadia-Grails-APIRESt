package com.ordenaris.internalControl

class App {
    
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    String url 
    Integer status = 1
    Date dateCreated
    Date lastUpdate
    String version
    String type //soap-scr-ssr
    String name
    String port
    String host
    
    static hasMany= [apis:Api]
    static belongsTo = Api
    
    static mapping = {
        version false
    }

    static constraints = {
        uuid maxSize:32

    }
}
