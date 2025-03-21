package com.ordenaris.internalControl

class Settings {
    String identifier
    String data
    
    static constraints = {
        identifier maxSize:50, unique:true
    }
    static mapping = {
        identifier index:"settings_identifier_idx"
        data index:"settings_identifier_idx"
        version false
    }
}