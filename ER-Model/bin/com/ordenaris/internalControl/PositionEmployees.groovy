package com.ordenaris.internalControl

class PositionEmployees {
    Date dateCreated
    Date lastUpdated
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
