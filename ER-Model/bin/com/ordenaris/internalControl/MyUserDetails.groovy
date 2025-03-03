package com.ordenaris.internalControl

<<<<<<< HEAD

import grails.plugin.springsecurity.userdetails.GrailsUser
import org.springframework.security.core.GrantedAuthority

=======
import grails.plugin.springsecurity.userdetails.GrailsUser
import org.springframework.security.core.GrantedAuthority

class MyUserDetails extends GrailsUser {

>>>>>>> b3d094ed72e530a5a9a125e6e8f6f1d70ff825f9
   MyUserDetails(String username, String password, boolean enabled,
                 boolean accountNonExpired, boolean credentialsNonExpired,
                 boolean accountNonLocked,
                 Collection<GrantedAuthority> authorities,
                 long id) {
      super(username, password, enabled, accountNonExpired,
            credentialsNonExpired, accountNonLocked, authorities, id)

<<<<<<< HEAD
   // }
}
=======
   }
}
>>>>>>> b3d094ed72e530a5a9a125e6e8f6f1d70ff825f9
