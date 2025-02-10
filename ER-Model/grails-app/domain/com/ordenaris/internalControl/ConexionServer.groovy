package com.ordenaris.internalControl

class ConnectionServer {
    Server server
    String description
    String portServ1
    String portServ2
    int status = 1
    Date dateCreated
    Date lastUpdated

    static constraints = {
        portServ1 unique: true,maxSize: 5
        portServ2 unique: true,maxSize: 5
        description blank:true, nullable:true
        lastUpdated blank: true, nullable: true
    }
    static mapping = {
        version false
    }
}
