package com.ordenaris.internalControl

class Configs {
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    String path
    String name
    Date dateCreated
    Date lastUpdated
    String type
    String description

    static mapping = {
        uuid index:"configs_uuid_idx"
        path index:"configs_path_idx"
        name index:"configs_name_idx"
        dateCreated index:"configs_dateCreated_idx"
        lastUpdated index:"configs_lastUpdated_idx"
        type index:"configs_type_idx"
        description index:"configs_description_idx"
        version false
    }

    static constraints = {
        uuid unique:true, maxSize:32
        lastUpdated nullable: true, blank:true
        name maxSize: 50
        description maxSize:150, nullable:true, blank:true
        type inList: [Constants.CONFIG_FILE_PROGRAM,Constants.CONFIG_FILE_SERVICE,Constants.CONFIG_FILE_APP,Constants.CONFIG_FILE_DATABASE,Constants.CONFIG_FILE_SERVER,Constants.CONFIG_FILE_BACKUP]
    }
}
