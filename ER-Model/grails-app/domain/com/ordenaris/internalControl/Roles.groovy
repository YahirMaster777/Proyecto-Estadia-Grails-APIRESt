package com.ordenaris.internalControl

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic
import java.util.UUID

@GrailsCompileStatic
@EqualsAndHashCode(includes='authority')
@ToString(includes='authority', includeNames=true, includePackage=false)
class Roles implements Serializable {
    // String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')

	private static final long serialVersionUID = 1

	String authority

	static constraints = {
		authority nullable: false, blank: false, unique: true
		// uuid maxSize:32, unique:true
	}

	static mapping = {
		cache true
		version false
	}
}
