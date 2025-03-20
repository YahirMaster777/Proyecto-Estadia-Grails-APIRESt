package com.ordenaris.internalControl

class Devs {
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    Employees dev
    Apps proyect
    String incumbency
    String statusDev ='activo'
    
    static mapping = {
        version false
        statusDev sqlType:"Enum('activo','inactivo')"
        incumbency sqlType:"Enum('baja','media', 'alta')"
    }
    static constraints = {
        uuid unique:true, maxSize:32
        incumbency inList:["baja","media","alta"]
        statusDev inList:["activo","inactivo"]
    }
}
