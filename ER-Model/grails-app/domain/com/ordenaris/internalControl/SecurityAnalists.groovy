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
        uuid unique:true
        
    }
    static maapping = {
        version false
    }
}
