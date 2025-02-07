package com.ordenaris.internalControl

class PositionEmployee {
    String name
    String description
    String area
    
    static mapping = {
        version false
    }

    static constraints = {
        name maxSize:60
        description maxSize:150
    }
}
