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

        def infoUsers = accessToken.principal.infoUsers
        println infoUsers
    
        def originalObject = [
            username         : user.username,
            nombre           : infoUsers.employee,
            uuid             : user.uuid,
            secciones        : infoUsers.secctions,
            roles            : accessToken.principal.authorities.authority,
            token_type       : 'bearer',
            access_token     : accessToken.accessToken,
            refresh_token    : accessToken.refreshToken,
            expiration       : accessToken.expiration
        ]
        return new JsonBuilder(originalObject).toPrettyString()
    }
}
