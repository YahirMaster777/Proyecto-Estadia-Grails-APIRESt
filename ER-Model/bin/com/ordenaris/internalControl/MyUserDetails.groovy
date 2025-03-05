package com.ordenaris.internalControl

import grails.plugin.springsecurity.userdetails.GrailsUser
import org.springframework.security.core.GrantedAuthority

class MyUserDetails extends GrailsUser {

   MyUserDetails(
      String username,
      String password,
      boolean enabled,
      boolean accountNonExpired,
      boolean credentialsNonExpired,
      boolean accountNonLocked,
      Collection<GrantedAuthority> authorities,
      long id
   ){super(
         username,
         password,
         enabled,
         accountNonExpired,
         credentialsNonExpired,
         accountNonLocked,
         authorities,
         id)
      
   }
}
