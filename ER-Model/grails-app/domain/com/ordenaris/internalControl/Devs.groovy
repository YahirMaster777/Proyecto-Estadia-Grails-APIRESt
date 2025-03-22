package com.ordenaris.internalControl

class Devs {
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    String incumbency
    String statusDev = Constants.STATUS_ACTIVE
    
    static belongsTo=[employee:Employees, project:Apps]
    
    static mapping = {
        uuid index:"devs_uuid_idx"
        statusDev  index:"devs_statusDev_idx"
        incumbency index:"devs_incumbency_idx"
        version false
    }
    static constraints = {
        uuid unique:true, maxSize:32
        incumbency inList:[Constants.INCUMBENCY_LOW,Constants.INCUMBENCY_MID,Constants.INCUMBENCY_HIGH]
        statusDev inList:[Constants.STATUS_ACTIVE,Constants.STATUS_INACTIVE]
    }
}
