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
    String company
    String position
    String area
    Date initialDate
    Integer idEmployee
    String nss
    String personalEmail
    String businessEmail
    String curp
    Integer status = 1
    Employee manage
    
    static mapping={
        version false
    }

    static constraints = {
        company inList:['Innovattia','Ordenaris', 'Pawerful']
        lastName2  nullable:true
        uuid maxSize:32, unique:true
        nss nullable: true
        manage nullable:true
        position nullable:true
        rfc nullable:true
        curp nullable:true
        businessEmail nullable:true
    }
}
