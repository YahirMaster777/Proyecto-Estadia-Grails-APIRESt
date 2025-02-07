package com.ordenaris.internalControl

class IntentRecovery {
    Date dateCreated
    String uuid = UUID.randomUUID().toString().replaceAll('//-', '')
    String host
    boolean used
    User user
    static mapping = {
        table name: "intent_recovery"
        version false
        used sqlType: "bit(1)"
    }
    static constraints = {
        host blank:true, nullable:true,maxSize:15
        uuid maxSize:255
    }
}
