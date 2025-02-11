package com.ordenaris.internalControl

class PositionEmployees {
    String name
    String description
    String area
    
    static mapping = {
        version false
    }

    static constraints = {
        name maxSize:50
        description maxSize:150
        area maxSize:30
    }
}
