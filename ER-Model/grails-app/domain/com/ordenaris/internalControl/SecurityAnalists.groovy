package com.ordenaris.internalControl
import java.util.UUID

    class SecurityAnalists {
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    String incumbency
    Employees responsible
    Servers server
    Date assigned
    Date lastUpdated
    String statusAnalist
    
    static constraints = {
        uuid unique:true, maxSize:32
        statusAnalist inList:['activo','inactivo']
        incumbency inList:['alta','media','baja']
    }
    static mapping = {
        version false
        statusAnalist sqlType:"Enum('activo','inactivo')"
        incumbency sqlType:"Enum('alta','media','baja')"
    }
}