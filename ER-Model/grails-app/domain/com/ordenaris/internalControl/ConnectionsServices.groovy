package com.ordenaris.internalControl

class ConnectionsServices {
    Programs program
    Programs service
    String description
    String portProgram
    String portService
    int status = 1
    Date dateCreated
    Date lastUpdated

    static constraints = {
        program nullable: false
        service nullable: false
        portProgram unique: true,maxSize: 5
        portService unique: true,maxSize: 5
        description blank:true, nullable:true, maxSize:150
        lastUpdated blank: true, nullable: true
    }

    static maapping = {
        version false
    }
}
