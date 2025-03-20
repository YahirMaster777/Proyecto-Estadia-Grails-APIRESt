package com.ordenaris.internalControl
import java.util.UUID

class PositionEmployees {
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
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
        uuid unique:true, maxSize:32
    }
}
