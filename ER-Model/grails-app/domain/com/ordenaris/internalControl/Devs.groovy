package com.ordenaris.internalControl

class Devs {
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    Employees dev
    Apps proyect
    Integer incumbency
    String status
    
    static mapping = {
        version false
        status sqlType:"Enum('Activo','Inactivo')"
    }
    static constraints = {
        uuid unique:true, maxSize:32
        incumbency maxSize:10
        status inList:["Activo","Inactivo"]
    }
}
