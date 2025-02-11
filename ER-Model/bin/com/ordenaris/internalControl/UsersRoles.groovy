package com.ordenaris.internalControl

import grails.gorm.DetachedCriteria
import groovy.transform.ToString

import org.codehaus.groovy.util.HashCodeHelper
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@ToString(cache=true, includeNames=true, includePackage=false)
class UsersRoles implements Serializable {

	private static final long serialVersionUID = 1

	Users user
	Roles role
	Sections section
	Permissions permission

	@Override
	boolean equals(other) {
		if (other instanceof UsersRoles) {
			other.userId == user?.id && other.roleId == role?.id
		}
	}

    @Override
	int hashCode() {
	    int hashCode = HashCodeHelper.initHash()
        if (user) {
            hashCode = HashCodeHelper.updateHash(hashCode, user.id)
		}
		if (role) {
		    hashCode = HashCodeHelper.updateHash(hashCode, role.id)
		}
		hashCode
	}

	static UsersRoles get(long userId, long roleId) {
		criteriaFor(userId, roleId).get()
	}

	static boolean exists(long userId, long roleId) {
		criteriaFor(userId, roleId).count()
	}

	private static DetachedCriteria criteriaFor(long userId, long roleId) {
		UsersRoles.where {
			user == Users.load(userId) &&
			role == Roles.load(roleId)
		}
	}

	static UsersRoles create(Users user, Roles role, boolean flush = false) {
		def instance = new UsersRoles(user: user, role: role)
		instance.save(flush: flush)
		instance
	}

	static boolean remove(Users u, Roles r) {
		if (u != null && r != null) {
			UsersRoles.where { user == u && role == r }.deleteAll()
		}
	}

	static int removeAll(Users u) {
		u == null ? 0 : UsersRoles.where { user == u }.deleteAll() as int
	}

	static int removeAll(Roles r) {
		r == null ? 0 : UsersRoles.where { role == r }.deleteAll() as int
	}

	static constraints = {
	    user nullable: false
		role nullable: false, validator: { Roles r, UsersRoles ur ->
			if (ur.user?.id) {
				if (UsersRoles.exists(ur.user.id, r.id)) {
				    return ['userRole.exists']
				}
			}
		}
	}

	static mapping = {
		id composite: ['user', 'role']
		version false
	}
}
