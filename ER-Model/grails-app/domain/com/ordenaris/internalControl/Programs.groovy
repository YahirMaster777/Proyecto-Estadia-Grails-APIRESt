package com.ordenaris.internalControl

class Programs {
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    String name
    String version
    String description    
    String port
    String type
    Date instalation
    Date dateCreated
    Date lastUpdated

    static belongsTo = [server:Servers]
    
    static mapping = {
        uuid index:"programs_uuid_idx"
        name index:"programs_name_idx"
        version index:"programs_version_idx"
        description index:"programs_description_idx"
        port index:"programs_port_idx"
        type index:"programs_type_idx"
        instalation index:"programs_instalation_idx"
        dateCreated index:"programs_dateCreated_idx"
        lastUpdated index:"Programs_lastUpdated_idx"
        version false
    }

    static constraints = {
        uuid unique: true, maxSize: 32
        port unique: true, maxSize: 5
        type inList:[Constants.CONFIG_FILE_PROGRAM,Constants.CONFIG_FILE_SERVICE]
        description blank:true, nullable:true, maxSize:150
        instalation blank:true, nullable: true
        lastUpdated blank:true, nullable: true
        name maxSize: 50
        version maxSize: 10
    }
}