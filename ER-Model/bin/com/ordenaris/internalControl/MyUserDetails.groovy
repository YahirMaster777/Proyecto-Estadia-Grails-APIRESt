package com.ordenaris.internalControl

import grails.plugin.springsecurity.userdetails.GrailsUser
import org.springframework.security.core.GrantedAuthority

class MyUserDetails extends GrailsUser {

   final String idEmployee

   MyUserDetails(String username, boolean enabled,
                 boolean accountNonExpired,
                 boolean accountNonLocked,
                 Collection<GrantedAuthority> authorities,
                 long id, long idEmployee) {
      super(username, enabled, accountNonExpired, accountNonLocked, authorities, id)

      this.idEmployee = idEmployee
   }
}