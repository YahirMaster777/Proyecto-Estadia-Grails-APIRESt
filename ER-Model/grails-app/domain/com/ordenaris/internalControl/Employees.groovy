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
    Integer identifierEmployee
    String nss
    String personalEmail
    String curp
    String status = Constants.STATUS_ACTIVE
    String description

    static belongsTo = [manage:Employees, company: Enterprises, position: PositionEmployees ]
    
    static mapping = {
        uuid index: "employees_uuid_idx"
        name index: "employees_name_idx"
        lastName1 index: "employees_lastName1_idx"
        lastName2 index: "employees_lastName2_idx"
        dateCreated index:"employees_dateCreated_idx"
        lastUpdated index: "employees_lastUpdated_idx"
        phone index:"employees_phone_idx"
        rfc index:"employees_rfc_idx"
        initialDate index:"employees_initialDate_idx"
        dismissedDate index:"employees_dimissedDate_idx"
        identifierEmployee index:"employees_identifierEmployee_idx"
        nss index: "employees_nss_idx"
        personalEmail index:"employees_personalEmail_idx"
        curp index:"employees_curp_idx"
        status index:"employees_status_idx"
        description index:"employees_description_idx"
        version false
    }

    static constraints = {
        status inList:[Constants.STATUS_ACTIVE,Constants.STATUS_INACTIVE,Constants.STATUS_SUSPENDED,Constants.STATUS_BREAK,Constants.STATUS_INABILITY]
        initialDate nullable:true, blank:true
        dismissedDate nullable:true, blank:true
        lastName2  nullable:true, blank:true, maxSize:30
        lastName1 maxSize:30
        uuid maxSize:32, unique:true
        nss maxSize:11
        phone maxSize:10, unique:true
        manage nullable:true,blank:true
        rfc  maxSize:13, unique:true
        curp maxSize:18, unique:true
        personalEmail email:true, maxSize:100, unique:true
        name maxSize:50
        description nullable:true, blank:true, maxSize:150
    }
}
