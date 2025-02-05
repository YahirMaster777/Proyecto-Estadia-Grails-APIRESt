package com.ordenaris.internalControl

class RolePermission {
    Role role
    Permissions permission
    
    static mapping ={
        version false
    }
    
    static constraints = {
        rol unique: ['permission']
    }
}
