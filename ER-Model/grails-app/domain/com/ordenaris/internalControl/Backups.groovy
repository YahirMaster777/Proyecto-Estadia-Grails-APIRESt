package com.ordenaris.internalControl

class Backups {
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    String description
    Date dateCreated
    String type
    String url
    
    static belongsTo = [server : Servers]
    
    static mapping = {
        uuid index:"backups_uuid_idx"
        description index:"backups_description_idx"
        dateCreated index:"backups_dateCreated_idx"
        url index:"backups_url_idx"
        type index:"backups_type_idx"
        version false
    }
    static constraints = {
        uuid unique: true,maxSize:32
        type inList:[Constants.TYPE_BACKUP_LOCAL,Constants.TYPE_BACKUP_CLOUD,Constants.TYPE_BACKUP_SERVER]
        url nullable: false, blank: false
        description maxSize:150, blank:true, nullable: true
    }
}
