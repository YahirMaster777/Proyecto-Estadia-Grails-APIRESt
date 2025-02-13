package com.ordenaris.internalControl

class ConnectionsServers {
    Servers server1
    Servers server2
    String description
    String portServ1
    String portServ2
    int status = 1
    Date dateCreated
    Date lastUpdated

    static constraints = {
        server nullable: false
        portServ1 unique: true,maxSize: 5
        portServ2 unique: true,maxSize: 5
        description blank:true, nullable:true, maxSize:150
        lastUpdated blank: true, nullable: true
    }
    static mapping = {
        version false
    }
}
