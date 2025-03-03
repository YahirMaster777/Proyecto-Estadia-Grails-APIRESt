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
    String businessEmail
    boolean enabled = false
    boolean accountExpired
    boolean accountLocked
    Employees employee
    boolean passwordExpired
<<<<<<< HEAD
    String flag
    Date dateLocked
=======
>>>>>>> b3d094ed72e530a5a9a125e6e8f6f1d70ff825f9
    Date lastLoginTime
    Date currentLoginDate

    Set<Roles> getAuthorities() {
        (UsersRoles.findAllByUser(this) as List<UsersRoles>)*.role as Set<Roles>
    }
    
    Set<Permissions> getPermissions(){
        (UserSectionPermission.findAllByUser(this) as List<UserSectionPermission>)*.permission as Set<Permissions>
    }    
    static constraints = {
        uuid unique:true, maxSize:32
        password password: true
        username nullable: false, blank: false, unique: true, maxSize:80
<<<<<<< HEAD
        businessEmail unique:true, maxSize:100, email:true, nullable: true, blank:true
        flag nullable:true, blank:true, maxSize:32
        dateLocked nullable:true, blank:true
=======
        businessEmail unique:true, maxSize:100, email:true, nullable: false, blank:true
        tk nullable: true, blank:true
        tkExpired nullable: true, blank:true
        employee nullable: true, blank:true
        lastLoginTime nullable:true, blank:true
        currentLoginDate nullable:true, blank:true
>>>>>>> b3d094ed72e530a5a9a125e6e8f6f1d70ff825f9
    }

    static mapping = {
	    password column: '`password`'
        version false
    }
}
