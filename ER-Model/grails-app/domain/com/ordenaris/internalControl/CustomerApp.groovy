package com.ordenaris.internalControl

class CustomerApp {
    Customer customer
    App app
    String description
    
    static constraints = {
        description blank:true, nullable:true
    }
    static mapping = {
        version false
    }
}
