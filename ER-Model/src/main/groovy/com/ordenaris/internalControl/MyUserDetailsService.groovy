package com.ordenaris.internalControl
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.userdetails.GrailsUserDetailsService
import grails.plugin.springsecurity.userdetails.NoStackUsernameNotFoundException
import grails.gorm.transactions.Transactional
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException

class MyUserDetailsService implements GrailsUserDetailsService {

    /**
     * Algunas clases de Spring Security (por ejemplo, RoleHierarchyVoter) esperan al menos
     * un rol, por lo que le damos a un usuario sin roles otorgados este que obtiene
     * superado esa restricción pero no concede nada.
     */
    static final List NO_ROLES = [new SimpleGrantedAuthority(SpringSecurityUtils.NO_ROLE)]

    UserDetails loadUserByUsername(String username, boolean loadRoles)
            throws UsernameNotFoundException {
        println "MyUserDetailsService -> $username"
        return loadUserByUsername(username)
    }

    @Transactional(readOnly=true, noRollbackFor=[IllegalArgumentException, UsernameNotFoundException])
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users user = Users.findByUsername(username)
        if (!user) throw new NoStackUsernameNotFoundException()

        def infoUsers = userService.infoUsers(user) // Asegúrate de tener acceso a userService para obtener infoUsers

        return new MyUserDetails(user.username, user.password, user.enabled,
                !user.accountExpired, !user.passwordExpired,
                !user.accountLocked, authorities ?: NO_ROLES, user.id, infoUsers)
    }
}
