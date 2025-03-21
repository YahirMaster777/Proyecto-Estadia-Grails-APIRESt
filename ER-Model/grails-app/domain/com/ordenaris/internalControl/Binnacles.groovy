package com.ordenaris.internalControl

class Binnacles {
    Date dateCreated
    String description
    String tableName
    String type
    String status = Constants.STATUS_OK
    
    static belongsTo = [user:Users]
    
    static mapping = {
        dateCreated index:"binnacles_dateCreated_idx"
        description index:"binnacles_description_idx"
        tableName index:"binnacles_tableName_idx"
        type index:"binncles_type_idx"
        status index:"binnacles_status_idx"
        version false
    }
    static constraints = {
        tableName maxSize: 40
        description maxSize:150, blank:true, nullable: true
        status inList: [Constants.STATUS_OK,Constants.STATUS_ERROR]
        type inList:[Constants.TYPE_ACTION_UPDATE, Constants.TYPE_ACTION_CREATE, Constants.TYPE_ACTION_DELETE]
    }
}