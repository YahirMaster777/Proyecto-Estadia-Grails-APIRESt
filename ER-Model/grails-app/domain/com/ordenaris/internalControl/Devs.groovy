package com.ordenaris.internalControl

class Devs {
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    Employees dev
    Apps proyect
    String incumbency
    String statusDev ='Activo'
    
    static mapping = {
        version false
        statusDev sqlType:"Enum('Activo','Inactivo')"
        incumbency sqlType:"Enum('Baja','Media', 'Alta')"
    }
    static constraints = {
        uuid unique:true, maxSize:32
        incumbency inList:["Baja","Media","Alta"]
        statusDev inList:["Activo","Inactivo"]
    }
}
