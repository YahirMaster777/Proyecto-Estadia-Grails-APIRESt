package com.ordenaris.internalControl

class Permission {
      //puede ser : read, create, update, delete, readPagination, readAll
    String name
    String description
    String alias

    
    static mapping ={
        version false
    }
    static constraints = {
        name unique: true, blank: false
        description maxSize:150
        alias maxSize:50
    }
}
