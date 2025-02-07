package com.ordenaris.internalControl

class Enterprise {
    String uuid = UUID.randomUUID().toString().replaceAll('//-','')
    String name
    String type //cliente o interna
    String description
    
    static mapping = {
        version false
    }
    static constraints = {
        uuid maxSize:32, unique:true
        type maxSize:60
    }
}
