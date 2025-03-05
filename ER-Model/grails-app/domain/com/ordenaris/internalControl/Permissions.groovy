package com.ordenaris.internalControl

class Permissions {
      //puede ser : read, create, update, delete, readPagination, readAll
    Date dateCreated
    String name
    String description
    String alias
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    Sections section
    
    static mapping ={
        version false
    }
    static constraints = {
        uuid unique:true, maxSize:32
        name maxSize:50
        description maxSize:150
        alias maxSize:50, unique: true
    }
}
