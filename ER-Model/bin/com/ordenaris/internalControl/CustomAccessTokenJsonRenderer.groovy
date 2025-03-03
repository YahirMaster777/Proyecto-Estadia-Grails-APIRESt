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

        def originalObject = [
            username         : user.username,
            role             : accessToken.principal.authorities[0].authority,
            access_token     : accessToken.accessToken,
            refresh_token    : accessToken.refreshToken,
            expiration       : accessToken.expiration,
            

        ]

        // if( accessToken.principal.authorities.size() == 1 ){
        //     def newRole = Role.findByAuthority( accessToken.principal.authorities[0] )
        //     // if(newRole.authority == "ROLE_ADMINISTRADOR"){
        //     //     originalObject.path = "${newRole.path}"
        //     // }else if(newRole.authority == "ROLE_SOPORTE"){
        //     //     originalObject.path = "${newRole.path}"
        //     // }else{
        //     //     originalObject.path = "/control${newRole.path}"
        //     // }
        //     // originalObject.perfil = newRole.nombre
        // }else{
        //     // if(newRole.authority == "ROLE_ADMINISTRADOR"){
        //     //     originalObject.path = "${newRole.path}"
        //     // }else if(newRole.authority == "ROLE_SOPORTE"){
        //     //     originalObject.path = "${newRole.path}"
        //     // }else{
        //     //     originalObject.path = "/control${newRole.path}"
        //     // }
        //     originalObject.perfil = newRole.nombre
        // }
        return new JsonBuilder(originalObject).toPrettyString()
    }

}
