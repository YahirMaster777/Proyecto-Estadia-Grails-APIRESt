package com.ordenaris.internalControl

class IntentRecovery {
    Date dateCreated
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    String used = Constants.STATUS_ACTIVE
    int intents
    Date dateExpired
    
    static belongsTo=[user:Users]
    
    static mapping = {
        dateCreated index:"intentRecovery_dateCreated_idx"
        uuid index:"intentRecovery_uuid_idx"
        used index:"intentRecovery_used_idx"
        intents index:"intentRecovery_intents_idx"
        dateExpired index:"intentRecovery_dateExpired_idx"
        version false
    }
    static constraints = {
        used inList:[Constants.STATUS_ACTIVE, Constants.STATUS_INACTIVE]
        uuid maxSize:32, unique:true
        dateExpired nullable:true, blank:true
    }
}
