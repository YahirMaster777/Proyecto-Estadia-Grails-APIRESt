package com.ordenaris.internalControl

class Customer {
    static hasMany = [customerApp: CustomerApp]
    
    String name
    int status = 1
    static constraints = {
        uuid unique: true,maxSize: 32
    }
    static mapping = {
        version false
    }
}
