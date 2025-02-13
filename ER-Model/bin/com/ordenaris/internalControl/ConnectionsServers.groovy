package com.ordenaris.internalControl
import java.util.UUID

class ConnectionsServers {
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    Servers server1
    Servers server2
    String description
    String portServ1
    String portServ2
    int status = 1
    Date dateCreated
    Date lastUpdated

    static constraints = {
        uuid unique:true, maxSize:32
        server1 nullable: false
        server2 nullable: false
        portServ1 unique: true,maxSize: 5
        portServ2 unique: true,maxSize: 5
        description blank:true, nullable:true, maxSize:150
        lastUpdated blank: true, nullable: true
    }
    static mapping = {
        version false
    }
}
