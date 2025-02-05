package com.ordenaris.internalControl

class Permissions {
    //puede ser : read, create, update, delete, readPagination, readAll
    String name 
    
    static mapping ={
        version false
    }
    
    
    static constraints = {
        name unique: true, blank: false

    }3
}
