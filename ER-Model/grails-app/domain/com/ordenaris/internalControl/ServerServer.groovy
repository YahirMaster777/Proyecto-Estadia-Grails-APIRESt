package com.ordenaris.internalControl

class ServerServer {
    static hasMany = [server: Server]
    Server server
    String description
    String port
    String service

    static constraints = {
        description blank:true, nullable:true
    }
    static mapping = {
        version false
    }
}
