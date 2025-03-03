package com.ordenaris.internalControl

import com.ordenaris.internalControl.Users
<<<<<<< HEAD
import com.ordenaris.internalControl.Employees
=======

>>>>>>> b3d094ed72e530a5a9a125e6e8f6f1d70ff825f9
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.rest.token.AccessToken
import grails.plugin.springsecurity.rest.token.rendering.AccessTokenJsonRenderer
import groovy.json.JsonBuilder
import org.springframework.security.core.GrantedAuthority

<<<<<<< HEAD
=======
@Transactional
class CustomAccessTokenJsonRenderer implements AccessTokenJsonRenderer {

>>>>>>> b3d094ed72e530a5a9a125e6e8f6f1d70ff825f9
    @Override
    String generateJson(AccessToken accessToken) {

        Users user = Users.get accessToken.principal.id as Long
<<<<<<< HEAD
        def originalObject = [
            username         : user.username,
            access_token     : accessToken.accessToken,
            expiration       : accessToken.expiration,
            role             : accessToken.principal.authorities[0].authority,
        ]
        return new JsonBuilder(originalObject).toPrettyString()
    }

        Users user = Users.get accessToken.principal.id as Long
        def originalObject = [
            username         : user.username,
            access_token     : accessToken.accessToken,
            expiration       : accessToken.expiration,
            role             : accessToken.principal.authorities[0].authority,
        ]
        return new JsonBuilder(originalObject).toPrettyString()
    }

        Users user = Users.get accessToken.principal.id as Long
=======
>>>>>>> b3d094ed72e530a5a9a125e6e8f6f1d70ff825f9

        def originalObject = [
            username         : user.username,
            access_token     : accessToken.accessToken,
            expiration       : accessToken.expiration,
            role             : accessToken.principal.authorities[0].authority

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
<<<<<<< HEAD
    // }

// }
=======
    }

}
>>>>>>> b3d094ed72e530a5a9a125e6e8f6f1d70ff825f9
