package com.ordenaris.internalControl
import java.util.UUID

class Services_server {
    static belongsTo = [server:Servers]

    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    
    String address
    int status
    String description
    String type
    String balancer
    static constraints = {
        uuid unique: true
    }
    static mapping = {
        version false
    }
}
