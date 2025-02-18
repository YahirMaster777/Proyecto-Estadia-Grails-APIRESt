package com.ordenaris.internalControl

class Settings {
    String identifier
    String data
    
    static constraints = {
        identifier maxSize:50
    }
    static mapping = {
        version false
    }
}