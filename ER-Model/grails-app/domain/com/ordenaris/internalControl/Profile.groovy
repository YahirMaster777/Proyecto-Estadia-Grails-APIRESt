package com.ordenaris.internalControl

class Profile {
    
    String uuid = UUID.randomUUID().toString().replaceAll('//-','')
    Date dateCreated
    Date lastUpdate
    Integer status = 1
    String name
    
    static hasMany=[permissions:RolePermission]
    static constraints = {
    }
}
