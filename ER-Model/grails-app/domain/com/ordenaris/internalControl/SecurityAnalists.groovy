package com.ordenaris.internalControl
import java.util.UUID

class SecurityAnalists {
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    int incumbency
    Employees responsible
    Servers server
    Date assigned
    Date lastUpdated
    int status = 1
    
    static constraints = {
        uuid unique:true, maxSize:32
        
    }
    static maapping = {
        version false
    }
}
