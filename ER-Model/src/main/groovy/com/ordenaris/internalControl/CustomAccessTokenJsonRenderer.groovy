package com.ordenaris.internalControl

import com.ordenaris.internalControl.Users

import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.rest.token.AccessToken
import grails.plugin.springsecurity.rest.token.rendering.AccessTokenJsonRenderer
import groovy.json.JsonBuilder
import org.springframework.security.core.GrantedAuthority

@Transactional
class CustomAccessTokenJsonRenderer implements AccessTokenJsonRenderer {

    @Override
    String generateJson(AccessToken accessToken) {

        Users user = Users.get accessToken.principal.id as Long
        def employee = Employees.get( accessToken.principal.idEmployee as Long )

        def originalObject = [
            username         : user.username.decrypt(),
            nombre           : "${employee.name.decrypt()}",   
            access_token     : accessToken.accessToken,
            sso: false
            // expiration       : accessToken.expiration
        ]

        if( accessToken.principal.authorities.size() == 1 ){
            def newRole = Roles.findByAuthority( accessToken.principal.authorities[0] )
            if(newRole.authority == "ROLE_ROOT"){
                originalObject.path = "${newRole.path.decrypt()}"
            }else if(newRole.authority == "ROLE_ADMIN"){
                originalObject.path = "${newRole.path.decrypt()}"
            }else{
                originalObject.path = "/control${newRole.path.decrypt()}"
            }
            originalObject.perfil = newRole.nombre.decrypt()
        }else{
            if(newRole.authority == "ROLE_ROOT"){
                originalObject.path = "${newRole.path.decrypt()}"
            }else if(newRole.authority == "ROLE_ADMIN"){
                originalObject.path = "${newRole.path.decrypt()}"
            }else{
                originalObject.path = "/control${newRole.path.decrypt()}"
            }
            originalObject.perfil = newRole.nombre.decrypt()
        }
        return new JsonBuilder(originalObject).toPrettyString()
    }

}
