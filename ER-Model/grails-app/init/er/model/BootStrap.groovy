package er.model

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import groovy.json.JsonBuilder;
import com.ordenaris.internalControl.Users;
import com.ordenaris.internalControl.UsersRoles;
import com.ordenaris.internalControl.Roles;
import com.ordenaris.internalControl.PositionEmployees;
import com.ordenaris.internalControl.Employees;
import com.ordenaris.internalControl.Enterprises;

class BootStrap {
    def init = { servletContext ->
        if (Roles.count() == 0) {
            def roleAdmin = new Roles(authority: 'ROLE_ADMIN').save(flush: true)// flush: true para que se guarde en la base de datos
            def roleRoot = new Roles(authority: 'ROLE_ROOT').save(flush: true)// flush: true para que se guarde en la base de datos
            def roleCustom = new Roles(authority: 'ROLE_CUSTOM').save(flush: true)// flush: true para que se guarde en la base de datos
            def userRoot1 = new Users(username: 'yairR', password: 'Yair141002')
            def userRoot2= new Users(username: 'emilioR', password: '1a2b3c4d')
            def userAdmin1 =  new Users(username: 'yairA', password: 'Yair141002')
            def userAdmin2 =  new Users(username: 'emilioA', password: '1a2b3c4d')
            def userCustom1 =  new Users(username: 'yairC', password: 'Yair141002')
            def userCustom2 =  new Users(username: 'emilioC', password: '1a2b3c4d')
            if (!userRoot1.save(flush: true) || !userRoot2.save(flush: true) || !userAdmin1.save(flush: true) || !userAdmin2.save(flush: true) || !userCustom1.save(flush: true) || !userCustom2.save(flush: true)) {
                userRoot1.errors.allErrors.each { println it }
                userRoot2.errors.allErrors.each { println it }
                userAdmin1.errors.allErrors.each { println it }
                userAdmin2.errors.allErrors.each { println it }
                userCustom1.errors.allErrors.each { println it }
                userCustom2.errors.allErrors.each { println it }
            } else {
                new UsersRoles(user: userRoot1, role: roleRoot).save(flush: true)
                new UsersRoles(user: userRoot2, role: roleRoot).save(flush: true)
                new UsersRoles(user: userAdmin1, role: roleAdmin).save(flush: true)
                new UsersRoles(user: userAdmin2, role: roleAdmin).save(flush: true)
                new UsersRoles(user: userCustom1, role: roleCustom).save(flush: true)
                new UsersRoles(user: userCustom2, role: roleCustom).save(flush: true)
            }
        }

        if (PositionEmployees.count() == 0) {
            def back = new PositionEmployees(name: 'Backend', description: 'Desarrollador backend', area: 'Desarrollo')
            def front = new PositionEmployees(name: 'Frontend', description: 'Desarrollador Frontend', area: 'Desarrollo')
            def ordenaris = new Enterprises(name: 'Ordenaris', type: 'Interna', description: 'Empresa de ecomerce')
            def innovattia = new Enterprises(name: 'Innovattia', type: 'Interna', description: 'Empresa')
            if (!back.save(flush:true) || !front.save(flush:true)  || !ordenaris.save(flush:true)  || !innovattia.save(flush:true)){
                back.errors.allErrors.each { println it }
                front.errors.allErrors.each { println it }
                ordenaris.errors.allErrors.each { println it }
                innovattia.errors.allErrors.each { println it }
            } else {
                new Employees(phone: '5519609544', curp: 'TOPM021014HPLLRRA0', idEmployee: 116, rfc: 'TOPM021014M62', lastName2: 'Perez', nss: '12345678911', position: back, company: innovattia, personalEmail: 'yair.ordenaris@gmail.com', name: 'Marvin Yair', lastName1: 'Tolentino', status: 'Inactivo').save(flush: true)
                new Employees(phone: '7781638560', curp: 'TOPM021014HPLLRR03', idEmployee: 1117, rfc: 'TOPM021014M32', lastName2: 'Perez', nss: '12345678912',position: front, company: innovattia, personalEmail: 'marvin.ordenaris@gmail.com', name: 'Marvin Yair', lastName1: 'Tolentino', status: 'Inactivo').save(flush: true)
                new Employees(phone: '7781638570', curp: 'TOPM021014HPLLRR01', idEmployee: 11172, rfc: 'TOPM021014M12', lastName2: 'Perez',nss: '12345678913', position: front, company: innovattia, personalEmail: 'marvin123.ordenaris@gmail.com', name: 'Marvin Yair', lastName1: 'Tolentino', status: 'Activo').save(flush: true)
                new Employees(phone: '7781638580', curp: 'TOPM021014HPLLRR21', idEmployee: 12172, rfc: 'TOPM021014M22', lastName2: 'Perez', nss: '12345678914', position: front, company: innovattia, personalEmail: 'marvin1213.ordenaris@gmail.com', name: 'Marvin Yair', lastName1: 'Tolentino', status: 'Activo').save(flush: true)
                new Employees(phone: '7641638580', curp: 'TOPM021014HPLLRRA1', idEmployee: 1, rfc: 'TOPM021014M01', lastName2: 'Perez', nss: '12345678915', position: front, company: innovattia, personalEmail: 'marvin1.ordenaris@gmail.com', name: 'Marvin Yair', lastName1: 'Tolentino', status: 'Activo').save(flush: true)
                new Employees(phone: '1234567891', curp: 'TOPM221155HPLLRRA1', idEmployee: 1, rfc: 'TOPM0221155M', lastName2: 'Lopez', nss: '12345678910', position: front, company: innovattia, personalEmail: 'juan.ordenaris@gmail.com', name: 'Juan', lastName1: 'Tolentino', status: 'Inactivo').save(flush: true)
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

