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
import com.ordenaris.internalControl.Settings
import com.ordenaris.internalControl.Sections;
import com.ordenaris.internalControl.Permissions;
import com.ordenaris.internalControl.UserSectionPermission;
import com.ordenaris.internalControl.Settings

class BootStrap {
    def init = { servletContext ->
        if (PositionEmployees.count() == 0) {
            new Settings(data: '30', identifier: 'MINUTES_OF_VALIDITY_CODE').save(flush:true)
<<<<<<< HEAD
            new Settings(data: '3', identifier: 'NUMBER_OF_RECOVERY_ATTEMPTS').save(flush:true)
=======
>>>>>>> aa85c83 (cambios del boostrapt)
            def back = new PositionEmployees(name: 'Backend', description: 'Desarrollador backend', area: 'Desarrollo')
            def front = new PositionEmployees(name: 'Frontend', description: 'Desarrollador Frontend', area: 'Desarrollo')
            def ordenaris = new Enterprises(name: 'Ordenaris', type: 'Interna', description: 'Empresa de ecomerce')
            def innovattia = new Enterprises(name: 'Innovattia', type: 'Interna', description: 'Empresa')
            def employee1 = new Employees(phone: '5519609544', curp: 'TOPM021014HPLLRRA0', idEmployee: 116, rfc: 'TOPM021014M62', lastName2: 'Perez', nss: '12345678911', position: back, company: innovattia, personalEmail: 'yair.ordenaris@gmail.com', name: 'Marvin Yair', lastName1: 'Tolentino', status: 'Inactivo')
            def employee2 = new Employees(phone: '7781638560', curp: 'TOPM021014HPLLRR03', idEmployee: 1117, rfc: 'TOPM021014M32', lastName2: 'Perez', nss: '12345678912',position: front, company: innovattia, personalEmail: 'marvin.ordenaris@gmail.com', name: 'Juan', lastName1: 'Tolentino', status: 'Inactivo')
            def employee3 = new Employees(phone: '7781638570', curp: 'TOPM021014HPLLRR01', idEmployee: 11172, rfc: 'TOPM021014M12', lastName2: 'Perez',nss: '12345678913', position: front, company: innovattia, personalEmail: 'marvin123.ordenaris@gmail.com', name: 'Adalid', lastName1: 'Tolentino', status: 'Activo')
            def employee4 = new Employees(phone: '7781638580', curp: 'TOPM021014HPLLRR21', idEmployee: 12172, rfc: 'TOPM021014M22', lastName2: 'Perez', nss: '12345678914', position: front, company: innovattia, personalEmail: 'marvin1213.ordenaris@gmail.com', name: 'Armando', lastName1: 'Tolentino', status: 'Activo')
            def employee5 = new Employees(phone: '7641638580', curp: 'TOPM021014HPLLRRA1', idEmployee: 1, rfc: 'TOPM021014M01', lastName2: 'Perez', nss: '12345678915', position: front, company: innovattia, personalEmail: 'marvin1.ordenaris@gmail.com', name: 'Luz', lastName1: 'Tolentino', status: 'Activo')
            def employee6 = new Employees(phone: '1234567891', curp: 'TOPM221155HPLLRRA1', idEmployee: 1, rfc: 'TOPM0221155M', lastName2: 'Lopez', nss: '12345678910', position: front, company: innovattia, personalEmail: 'juan.ordenaris@gmail.com', name: 'Luis', lastName1: 'Tolentino', status: 'Inactivo')
            if (!back.save(flush:true) || !front.save(flush:true)  || !ordenaris.save(flush:true)  || !innovattia.save(flush:true)){
                back.errors.allErrors.each { println it }
                front.errors.allErrors.each { println it }
                ordenaris.errors.allErrors.each { println it }
                innovattia.errors.allErrors.each { println it }
            } else {
                employee1.save(flush:true)
                employee2.save(flush:true)
                employee3.save(flush:true)
                employee4.save(flush:true)
                employee5.save(flush:true)
                employee6.save(flush:true)
            }
            def roleAdmin = new Roles(authority: 'ROLE_ADMIN').save(flush: true)
            def roleRoot = new Roles(authority: 'ROLE_ROOT').save(flush: true)
            def roleCustom = new Roles(authority: 'ROLE_CUSTOM').save(flush: true)
            
            def section1 = new Sections(url:'internalControl.com', name:'Aplicaciones', description:'Seccion que permite ver todo lo relacionado con las aplicaciones').save(flush:true)
            def section2 = new Sections(url:'internalControl.com', name:'Servidores', description:'Seccion que permite ver todo lo relacionado con los servidores').save(flush:true)
            def section3 = new Sections(url:'internalControl.com', name:'Empleados', description:'Seccion que permite ver todo lo relacionado con los empleados').save(flush:true)
            
            def section1permission1 = new Permissions(alias:'create_app', section:section1,uuidSection:section1.uuid, name:'Crear Apps',description:'Permiso que permite crear apps').save(flush:true)
            def section1permission2 = new Permissions(alias:'delete_app', section:section1,uuidSection:section1.uuid, name:'Eliminar Apps',description:'Permiso que permite eliminar apps').save(flush:true)
            def section1permission3 = new Permissions(alias:'edit_app', section:section1,uuidSection:section1.uuid, name:'Editar Apps',description:'Permiso que permite editar apps').save(flush:true)
            def section1permission4 = new Permissions(alias:'view_app', section:section1,uuidSection:section1.uuid, name:'Ver Apps',description:'Permiso que permite ver apps').save(flush:true)
            
            def section2permission1 = new Permissions(alias:'create_server', section:section2,uuidSection:section2.uuid, name:'Crear servidor',description:'Permiso que permite').save(flush:true)
            def section2permission2 = new Permissions(alias:'edit_server', section:section2,uuidSection:section2.uuid, name:'Editar servidor',description:'Permiso que permite').save(flush:true)
            def section2permission3 = new Permissions(alias:'delete_server', section:section2,uuidSection:section2.uuid, name:'Eliminar servidor',description:'Permiso que permite').save(flush:true)
            def section2permission4 = new Permissions(alias:'view_server', section:section2,uuidSection:section2.uuid, name:'Ver servidores',description:'Permiso que permite').save(flush:true)
            
            def section3permission1 = new Permissions(alias:'create_employee', section:section3,uuidSection:section3.uuid, name:'Crear Empleados',description:'Permiso que permite').save(flush:true)
            def section3permission2 = new Permissions(alias:'delete_employee', section:section3,uuidSection:section3.uuid, name:'Eliminar Empleados',description:'Permiso que permite').save(flush:true)
            def section3permission3 = new Permissions(alias:'edit_employee', section:section3,uuidSection:section3.uuid, name:'Editar Empleados',description:'Permiso que permite').save(flush:true)
            def section3permission4 = new Permissions(alias:'view_employee', section:section3,uuidSection:section3.uuid, name:'Ver Empleados',description:'Permiso que permite').save(flush:true)
    
            def userRoot1 = new Users(username: 'yairR', password: 'Yair141002',   businessEmail:'yairR@gmail.com', employee:employee1)
            def userRoot2= new Users(username: 'emilioR', password: '1a2b3c4d',  businessEmail:'emilioR@gmail.com', employee:employee2)
            def userAdmin1 =  new Users(username: 'yairA', password: 'Yair141002', businessEmail:'yairA@gmail.com', employee:employee3)
            def userAdmin2 =  new Users(username: 'emilioA', password: '1a2b3c4d', businessEmail:'emilioA@gmail.com', employee:employee4)
            def userCustom1 =  new Users(username: 'yairC', password: 'Yair141002',  businessEmail:'yairC@gmail.com', employee:employee5)
            def userCustom2 =  new Users(username: 'emilioC', password: '1a2b3c4d', businessEmail:'emilioC@gmail.com', employee:employee6)
            new Users(username: 'emilio.mendoza@ordenaris.com', password: '1a2b3c4d', businessEmail:'emilioT@gmail.com', employee:employee2).save(flush:true)
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
            
            def listUserPermission1 = new UserSectionPermission (section: section1, permission: section1permission1, user: userRoot1).save(flush:true)
            def listUserPermission2 = new UserSectionPermission (section: section1, permission: section1permission2, user: userRoot1).save(flush:true)
            def listUserPermission3 = new UserSectionPermission (section: section1, permission: section1permission3, user: userRoot1).save(flush:true)
            def listUserPermission4 = new UserSectionPermission (section: section1, permission: section1permission3, user: userRoot1).save(flush:true)
            
        }
        def munutsOfValidCode = Settings.findByIdentifier('MINUTES_OF_VALIDITY_CODE')
        servletContext.setAttribute('MINUTES_OF_VALIDITY_CODE', munutsOfValidCode.data)
        def numberOfRecoveryAttempts = Settings.findByIdentifier('NUMBER_OF_RECOVERY_ATTEMPTS')
        servletContext.setAttribute('NUMBER_OF_RECOVERY_ATTEMPTS', numberOfRecoveryAttempts.data)
        
        String.metaClass.formatHour = {
            def horaCodeExpression = '^([0-1][1-9]|[2][0-3])(:)([0-5][0-9])(:)([0-5][0-9])$'
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
        String.metaClass.validNss = {
        def pageExpression = '^\\d{11}$'
            def pattern = Pattern.compile(pageExpression)
            def matcher = pattern.matcher(delegate)
            return matcher.matches()
        }
        String.metaClass.uuidFormat = {
            def pageExpression = '^[a-fA-F0-9]{32}$'
            def pattern = Pattern.compile(pageExpression)
            def matcher = pattern.matcher(delegate)
            return matcher.matches()
        }
        String.metaClass.specialCharacters = {
            def pageExpression = '^[a-zA-Z0-9\\s]+$'
            def pattern = Pattern.compile(pageExpression)
            def matcher = pattern.matcher(delegate)
            return matcher.matches()
        }
        String.metaClass.validPassword = {
            def pageExpression = '^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])([A-Za-z\\d$@$!%*?&]|[^ ]){8,40}$'
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
        String.metaClass.macAddress = {
            def pageExpression = "^([0-9A-Fa-f]{2}[\\:-]){5}([0-9A-Fa-f]{2})\$"
            def pattern = Pattern.compile(pageExpression)
            def matcher = pattern.matcher(delegate)
            return matcher.matches()
        }
        String.metaClass.ipAddress = {
            def pageExpression = "^(\\b25[0-5]|\\b2[0-4][0-9]|\\b[01]?[0-9][0-9]?)(\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}\$"
            def pattern = Pattern.compile(pageExpression)
            def matcher = pattern.matcher(delegate)
            return matcher.matches()
        }
        String.metaClass.validPort = {
            def pageExpression = '^(\\b6553[0-5]|\\b655[0-2]\\d|\\b65[0-4]\\d{2}|\\b6[0-4]\\d{3}|\\b[1-5]\\d{4}|\\d{1,4})$'
            def pattern = Pattern.compile(pageExpression)
            def matcher = pattern.matcher(delegate)
            return matcher.matches()
        }
        String.metaClass.institutionalEmail = {
            def pageExpression = "^[a-zA-Z0-9\\.]+@[\\w\\.]+\\.[\\w]{3}\$"
            def pattern = Pattern.compile(pageExpression)
            def matcher = pattern.matcher(delegate)
            return matcher.matches()
        }
        String.metaClass.personalEmail = {
            def pageExpression = "^[\\w\\%*.=-]+@[\\w\\.]+\\.[\\w]{3}\$"
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
        
        String.metaClass.validPort = {
            def pageExpression = '^(\\b6553[0-5]|\\b655[0-2]\\d|\\b65[0-4]\\d{2}|\\b6[0-4]\\d{3}|\\b[1-5]\\d{4}|\\d{1,4})$'
            def pattern = Pattern.compile(pageExpression)
            def matcher = pattern.matcher(delegate)
            return matcher.matches()
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

