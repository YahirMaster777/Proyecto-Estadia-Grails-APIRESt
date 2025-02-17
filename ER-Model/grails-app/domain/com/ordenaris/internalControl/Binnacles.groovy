package com.ordenaris.internalControl

class Binnacles {
    Date dateCreated
    String description
    Users user
    String tableName
    String type
    String status = 'Ok'

    static mapping = {
        version false
        status sqlType:"Enum('Ok','Error')"
        type sqlType:"Enum('Actualización','Creación','Eliminación')"
    }
    static constraints = {
        tableName maxSize: 20
        description maxSize:150, blank:true, nullable: true
        status inList: ['Ok','Error']
        type inList:['Actualización','Creación','Eliminación']
    }
}
