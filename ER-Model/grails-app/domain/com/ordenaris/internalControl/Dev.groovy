package com.ordenaris.internalControl

class Dev {
    String uuid = UUID.randomUUID().toString().replaceAll('//-', '')
    Employee dev
    App proyect
    Integer incumbency
    
    static mapping = {
        version false
    }
    static constraints = {
        uuid unique:true, maxSize:32
        incumbency maxSize:10
    }
}
