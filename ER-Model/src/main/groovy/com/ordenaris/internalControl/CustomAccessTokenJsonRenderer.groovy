package com.ordenaris.internalControl

import com.ordenaris.internalControl.Users
import com.ordenaris.internalControl.Employees
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
            access_token     : accessToken.accessToken,
            expiration       : accessToken.expiration,
            role             : accessToken.principal.authorities[0].authority,
        ]
        return new JsonBuilder(originalObject).toPrettyString()
    }

}
