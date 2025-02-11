package com.ordenaris.internalControl

class ConnectionsServers {
    Servers server
    String description
    String portServ
    int status = 1
    Date dateCreated
    Date lastUpdated

    static constraints = {
        portServ unique: true,maxSize: 5
        description blank:true, nullable:true
        lastUpdated blank: true, nullable: true
    }
    static mapping = {
        version false
    }
}
