package com.ordenaris.internalControl

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic
import java.util.UUID

@GrailsCompileStatic
@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=false)
class Users implements Serializable {

    private static final long serialVersionUID = 1
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    String username
    String password
    String businessEmail
    String tk
    boolean tkExpired 
    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    Employees employee
    boolean passwordExpired

    Set<Roles> getAuthorities() {
        (UsersRoles.findAllByUser(this) as List<UsersRoles>)*.role as Set<Roles>
    }

    static constraints = {
        uuid unique:true, maxSize:32
        password password: true
        username nullable: false, blank: false, unique: true, maxSize:80
        businessEmail unique:true, maxSize:100, email:true, nullable: false, blank:true
        tk nullable: true, blank:true
        tkExpired nullable: true, blank:true
    }

    static mapping = {
	    password column: '`password`'
        version false
    }
}
