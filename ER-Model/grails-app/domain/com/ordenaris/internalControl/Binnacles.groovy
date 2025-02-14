package com.ordenaris.internalControl

class Binnacles {
    Date dateCreated
    String description
    Users user
    String tableName
    String extra
    String url
    String statis

    static constraints = {
        extra blank:true, nullable:true
        tableName maxSize: 20
        url nullable: false, blank: false
        description maxSize:150, blank:true, nullable: true
        status inList: ["Bien", "Mal", "Suspendido"]
    }
    
    static mapping = {
        version false
    }
}
