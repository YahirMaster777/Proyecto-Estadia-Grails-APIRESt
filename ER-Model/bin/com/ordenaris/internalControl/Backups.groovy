package com.ordenaris.internalControl

class Backups {
    Servers server
    Configs locationConfig
    String description
    int status = 1
    Date dateCreated
    Date lastUpdated
    //en formato {local, nube o servidor}, ruta
    String type
    String url
    static mapping = {
        version false
        type sqlType:"Enum('Local','Nube','Servidor')"
    }
    static constraints = {
        lastUpdated blank: true, nullable: true
        url nullable: false, blank: false
        description maxSize:150, blank:true, nullable: true
        type inList:['Local','Nube','Servidor']
    }
}
