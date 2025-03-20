package com.ordenaris.internalControl

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic
import java.util.UUID

@GrailsCompileStatic
@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=true)
class Users implements Serializable {

    private static final long serialVersionUID = 1
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    String username
    String password
    boolean enabled = false
    boolean accountExpired
    boolean accountLocked
    Employees employee
    boolean passwordExpired
    Date dateLocked
   

    Set<Roles> getAuthorities() {
        (UsersRoles.findAllByUser(this) as List<UsersRoles>)*.role as Set<Roles>
    }
    
    Set<Permissions> getPermissions(){
        (UserSectionPermission.findAllByUser(this) as List<UserSectionPermission>)*.permission as Set<Permissions>
    }    
    static constraints = {
        uuid unique:true, maxSize:32
        password password: true, nullable:true, blank:true
        username nullable: false, blank: false, unique: true, maxSize:80
        dateLocked nullable:true, blank:true
    }

    static mapping = {
	    password column: '`password`'
        version false
    }
}
