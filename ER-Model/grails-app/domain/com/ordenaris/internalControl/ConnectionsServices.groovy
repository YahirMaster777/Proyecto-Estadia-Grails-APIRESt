package com.ordenaris.internalControl

class ConnectionsServices {
    Programs program
    Programs service
    String portProgram
    String portService
    String status
    Date dateCreated
    Date lastUpdated

    static constraints = {
        program nullable: false
        service nullable: false
        portProgram unique: true,maxSize: 5
        portService unique: true,maxSize: 5
        lastUpdated blank: true, nullable: true
        status inList:['Activa','Inactiva', 'Matenimiento', 'Pruebas']
    }

    static mapping = {
        version false
        status sqlType:"Enum('Activa','Inactiva', 'Matenimiento', 'Pruebas')"
    }   
}
