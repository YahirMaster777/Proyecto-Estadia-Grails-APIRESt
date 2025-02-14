package com.ordenaris.internalControl

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=false)
class Users implements Serializable {

    private static final long serialVersionUID = 1

    String username
    String password
    String businessEmail
    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired
    Employees employee

    Set<Roles> getAuthorities() {
        (UsersRoles.findAllByUser(this) as List<UsersRoles>)*.role as Set<Roles>
    }

    static constraints = {
        password nullable: false, blank: false, password: true
        username nullable: false, blank: false, unique: true, maxSize:80
        businessEmail unique:true, maxSize:150, email:true
    }

    static mapping = {
	    password column: '`password`'
    }
}
