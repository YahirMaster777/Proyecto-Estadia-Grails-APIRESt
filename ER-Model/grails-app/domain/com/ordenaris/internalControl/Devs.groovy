package com.ordenaris.internalControl

class Devs {
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    Employees dev
    Apps proyect
    Integer incumbency
    String statusDev ='Activo'
    
    static mapping = {
        version false
        statusDev sqlType:"Enum('Activo','Inactivo')"
    }
    static constraints = {
        uuid unique:true, maxSize:32
        incumbency maxSize:10
        statusDev inList:["Activo","Inactivo"]
    }
}
