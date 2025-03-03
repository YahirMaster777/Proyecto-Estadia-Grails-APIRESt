package com.ordenaris.internalControl

class serverConnections {
    Servers server
    Servers subServer
    String portServer
    String portSubServer
    String status
    Date dateCreated
    Date lastUpdated

    static constraints = {
        server nullable: false
        subServer nullable: false
        portServer unique: true,maxSize: 5
        portSubServer unique: true,maxSize: 5
        lastUpdated blank: true, nullable: true
        status inList:['Activa','Inactiva', 'Matenimiento', 'Pruebas']
    }
    static mapping = {
        version false
        status sqlType:"Enum('Activa','Inactiva', 'Matenimiento', 'Pruebas')"

    }
}
