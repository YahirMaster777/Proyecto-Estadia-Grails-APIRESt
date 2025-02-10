package com.ordenaris.internalControl

class Employee {
    String uuid = UUID.randomUUID().toString().replaceAll('//-', '')
    String name
    String lastName1
    String lastName2
    Date dateCreated
    Date lastUpdated
    String phone
    String rfc
    Date initialDate
    Integer idEmployee
    String nss
    String personalEmail
    String businessEmail
    String curp
    Integer status = 1
    Employee manage
    Enterprise company
    PositionEmployee position
    
    static mapping={
        version false
    }

    static constraints = {
        company inList:['Innovattia','Ordenaris', 'Pawerful']
        initialDate nullable:true, blank:true
        lastName2  nullable:true, blank:true, maxSize:30
        lastName1 maxSize:30
        uuid maxSize:32, unique:true
        nss nullable: true, maxSize:11
        phone maxSize:15
        manage nullable:true
        rfc nullable:true, maxSize:13
        curp nullable:true, maxSize:18
        businessEmail nullable:true, email:true
        personalEmail email:true
        name maxSize:40
    }
}
