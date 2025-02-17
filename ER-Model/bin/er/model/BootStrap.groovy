package er.model

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import groovy.json.JsonBuilder;
import com.ordenaris.internalControl.Users;
import com.ordenaris.internalControl.UsersRoles;
import com.ordenaris.internalControl.Roles;

class BootStrap {
    def init = { servletContext ->
        if (Roles.count() == 0) {
            def roleAdmin = new Roles(authority: 'ROLE_ADMIN').save(flush: true)// flush: true para que se guarde en la base de datos
            def roleRoot = new Roles(authority: 'ROLE_ROOT').save(flush: true)// flush: true para que se guarde en la base de datos
            def roleCustom = new Roles(authority: 'ROLE_CUSTOM').save(flush: true)// flush: true para que se guarde en la base de datos
            def userRoot1 = new Users(username: 'yair.ordenaris@gmail.com', password: 'Yair141002')
            def userRoot2= new Users(username: 'emilio.ordenaris@gmail.com', password: '1a2b3c4d')
            if (!userRoot1.save(flush: true) || !userRoot2.save(flush: true)) {
                userRoot1.errors.allErrors.each { println it }
                userRoot2.errors.allErrors.each { println it }
            } else {
                new UsersRoles(user: userRoot1, role: roleRoot).save(flush: true)
                new UsersRoles(user: userRoot2, role: roleRoot).save(flush: true)
            }
        }
        
        String.metaClass.validFormatDataHour = {
            def horaCodeExpression = '([0-1][1-9]|[2][0-3])(:)([0-5][0-9])$'
            def pattern = Pattern.compile(horaCodeExpression) 
            def matcher = pattern.matcher( delegate ) 
            return matcher.matches()  
        }
        String.metaClass.onlyInt = {
            def pageExpression = '^\\d+$'
            def pattern = Pattern.compile(pageExpression)
            def matcher = pattern.matcher(delegate)
            return matcher.matches()
        }
        String.metaClass.phoneNumber = {
        def pageExpression = '^\\d{10}$'
            def pattern = Pattern.compile(pageExpression)
            def matcher = pattern.matcher(delegate)
            return matcher.matches()
        }
        String.metaClass.uuidFormat = {
            def pageExpression = '^[a-f0-9]{32}$'
            def pattern = Pattern.compile(pageExpression)
            def matcher = pattern.matcher(delegate)
            return matcher.matches()
        }
        String.metaClass.specialCharacters = {
            def pageExpression = '^[a-zA-Z0-9]+$'
            def pattern = Pattern.compile(pageExpression)
            def matcher = pattern.matcher(delegate)
            return matcher.matches()
        }
        String.metaClass.onlyDouble = {
            def pageExpression = "^[0-9]+(.[0-9]+)?\$"
            def pattern = Pattern.compile(pageExpression)
            def matcher = pattern.matcher(delegate)
            return matcher.matches()
        }
        String.metaClass.validarPathImg = {
            // delegate.matches(/^\/.*\.webp$/)
            def pageExpression = '/^\\/.*\\.webp$/'
            def pattern = Pattern.compile(pageExpression)
            def matcher = pattern.matcher(delegate)
            return matcher.matches()
        }
        Date.metaClass.log = {
            delegate.format("yyyy-MM-dd HH:mm:ss")
        }
        Object.metaClass.toPrettyString = {
            try {
                return new JsonBuilder(delegate).toPrettyString().replaceAll('\n', '').replaceAll('    ', '')
            }catch(e) {
                return '{ERROR-AL-GENERAL-JSON}'
            }
        }
    }
    def destroy = {
    }
}

