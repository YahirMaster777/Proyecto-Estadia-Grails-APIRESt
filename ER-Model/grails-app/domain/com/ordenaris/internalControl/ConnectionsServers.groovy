package com.ordenaris.internalControl

class ConnectionsServers {
    Servers server
    Servers subServer
    String description
    String portServer
    String portSubServer
    int status = 1
    Date dateCreated
    Date lastUpdated

    static constraints = {
        server nullable: false
        subServer nullable: false
        portServer unique: true,maxSize: 5
        portSubServer unique: true,maxSize: 5
        description blank:true, nullable:true, maxSize:150
        lastUpdated blank: true, nullable: true
    }
    static mapping = {
        version false
    }
}
