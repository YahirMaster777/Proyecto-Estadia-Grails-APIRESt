package com.ordenaris.internalControl

class IntentRecovery {
    Date dateCreated
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    String host
    boolean used
    Users user
    static mapping = {
        version false
        used sqlType: "bit(1)"
    }
    static constraints = {
        host blank:true, nullable:true,maxSize:15
        uuid maxSize:32, unique:true
    }
}
