package com.ordenaris.internalControl

class Customer {
    static hasMany = [customerApp: CustomerApp]
    
    String name
    int status
    static constraints = {
        uuid unique: true
    }
    static mapping = {
        version false
    }
}
