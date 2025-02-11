package com.ordenaris.internalControl

class Permissions {
      //puede ser : read, create, update, delete, readPagination, readAll
    String name
    String description
    String alias

    
    static mapping ={
        version false
    }
    static constraints = {
        name unique: true, blank: false, maxSize:50
        description maxSize:150
        alias maxSize:50
    }
}
