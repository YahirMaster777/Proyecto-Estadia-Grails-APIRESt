package com.ordenaris.internalControl

class Binnacles {
    Date dateCreated
    String description
    Users user
    String tableName
    String url
    String status

    static mapping = {
        version false
        status sqlType:"Enum('Bien','Mal','Suspendido')"
    }
    static constraints = {
        tableName maxSize: 20
        url nullable: false, blank: false
        description maxSize:150, blank:true, nullable: true
        status inList: ['Bien','Mal','Suspendido']
    }
}
