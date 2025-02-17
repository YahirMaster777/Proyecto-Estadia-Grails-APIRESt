package com.ordenaris.internalControl
import java.util.UUID

class SecurityAnalists {
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    int incumbency
    Employees responsible
    Servers server
    Date assigned
    Date lastUpdated
    String status
    
    static constraints = {
        uuid unique:true, maxSize:32
        status inList:['Activo','Inactivo']
    }
    static maapping = {
        version false
        status sqlType:"Enum('Activo','Inactivo')"
    }
}
