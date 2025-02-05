package com.ordenaris.internalControl

class Devs {
    
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    String role
    User user
    App app
    Api api
    
    static mapping ={
        version false
    }
    

    static constraints = {
        app nullable:true
        api nullable:true
        
    }
    
    
}
