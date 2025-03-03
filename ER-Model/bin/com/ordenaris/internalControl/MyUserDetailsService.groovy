package com.ordenaris.internalControl
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.userdetails.GrailsUserDetailsService
import grails.plugin.springsecurity.userdetails.NoStackUsernameNotFoundException
import grails.gorm.transactions.Transactional
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
class MyUserDetailsService implements GrailsUserDetailsService {

   @Transactional(readOnly=true, noRollbackFor=[IllegalArgumentException, UsernameNotFoundException])
   UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

      Users user = Users.findByUsername(username)
      if (!user) throw new NoStackUsernameNotFoundException()

      def roles = user.authorities




//       def authorities = roles.collect {
//          new SimpleGrantedAuthority(it.authority)
//       }

      return new MyUserDetails(user.username, user.password, user.enabled,
            !user.accountExpired, !user.passwordExpired,
            !user.accountLocked, authorities ?: NO_ROLES, user.id)
   }
}
