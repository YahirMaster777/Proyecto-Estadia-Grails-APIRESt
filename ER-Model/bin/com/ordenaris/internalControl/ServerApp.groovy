package com.ordenaris.internalControl

<<<<<<< HEAD
class ServersApps {
    static belongsTo = [server: Servers, app: Apps]
    Servers server
=======
class ServerApp {
    static belongsTo = [server: Server, app: Apps]
    Server server
>>>>>>> prueba
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
        description blank:true, nullable:true, maxSize:150
        lastUpdated blank: true, nullable: true
        service maxSize: 50
    }
    static mapping = {
        version false
    }
}
