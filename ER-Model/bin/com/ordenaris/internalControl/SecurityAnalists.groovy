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
        statusAnalist inList:['Activo','Inactivo']
        incumbency inList:['Alta','Media','Baja']
    }
    static mapping = {
        version false
        statusAnalist sqlType:"Enum('Activo','Inactivo')"
        incumbency sqlType:"Enum('Alta','Media','Baja')"
    }
}