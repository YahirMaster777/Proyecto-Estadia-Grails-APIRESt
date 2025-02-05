package com.ordenaris.internalControl

class IntentRecovery {

    Date date_created = new Date()
    String uuid = UUID.randomUUID().toString()
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
