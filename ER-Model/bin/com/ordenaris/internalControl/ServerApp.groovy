package com.ordenaris.internalControl

class ServerApp {
    static belongsTo = [server: Server, app: App]
    Server server
    String description
    String portApp
    String portServ
    String service
    int status = 1
    Date dateCreated
    Date lastUpdated

    static constraints = {
        portApp unique: true,maxSize: 5
        portServ unique: true,maxSize: 5
        description blank:true, nullable:true
        lastUpdated blank: true, nullable: true
        service maxSize: 50
    }
    static mapping = {
        version false
    }
}
