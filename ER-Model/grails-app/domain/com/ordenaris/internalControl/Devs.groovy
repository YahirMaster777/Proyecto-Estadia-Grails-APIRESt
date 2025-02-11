package com.ordenaris.internalControl

class Devs {
    String uuid = UUID.randomUUID().toString().replaceAll('//-', '')
    Employees dev
    Apps proyect
    Integer incumbency
    
    static mapping = {
        version false
    }
    static constraints = {
        uuid unique:true, maxSize:32
        incumbency maxSize:10
    }
}
