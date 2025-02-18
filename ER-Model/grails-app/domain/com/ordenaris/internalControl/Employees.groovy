package com.ordenaris.internalControl

class Employees {
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    String name
    String lastName1
    String lastName2
    Date dateCreated
    Date lastUpdated
    String phone
    String rfc
    Date initialDate
    Date dismissedDate
    Integer idEmployee
    String nss
    String personalEmail
    String curp
    String status = "Activo"
    Employees manage
    Enterprises company
    PositionEmployees position
    
    static mapping={
        version false
        status sqlType:"Enum('Activo', 'Inactivo','Suspendido','Descanso','Incapacitado')"
    }

    static constraints = {
        status inList:["Activo", "Inactivo","Suspendido","Descanso","Incapacitado"]
        initialDate nullable:true, blank:true
        dismissedDate nullable:true, blank:true
        lastName2  nullable:true, blank:true, maxSize:30
        lastName1 maxSize:30
        uuid maxSize:32, unique:true
        nss nullable: true, maxSize:11
        phone maxSize:10, unique:true
        manage nullable:true,blank:true
        rfc nullable:true, maxSize:13, blank:true, unique:true
        curp nullable:true, maxSize:18, blank:true, unique:true
        personalEmail email:true, maxSize:100, unique:true
        name maxSize:50
    }
}
