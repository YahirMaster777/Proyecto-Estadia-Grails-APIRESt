package com.ordenaris.internalControl

class Settings {
    String identifier
    String data
    
    static constraints = {
        identifier maxSize:50, unique:true
    }
    static mapping = {
        version false
    }
}