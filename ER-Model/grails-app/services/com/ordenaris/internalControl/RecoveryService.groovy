package com.ordenaris.internalControl

import grails.gorm.transactions.Transactional
import groovy.time.TimeCategory
import groovy.time.TimeDuration
import groovyx.net.http.Method
import java.util.UUID

@Transactional
class RecoveryService {
    def grailsApplication
    def minExpired = Setting.get(Setting.MINUTES_VALIDITY_CODE)
    def numberIntents = Setting.get(Setting.NUMBER_RECOVERY_ATTEMPTS)

    def createToken(username, logId, flag = false) {
        Users.withTransaction{ uStatus->
            try {
                def user = Users.findByUsername(username)
                // TODO modificar para tambien gestionar la cuenta de empleados
                def intent = IntentRecovery.findByUser(user)
                if (user && user.dateLocked) {
                    TimeDuration timeDiff = TimeCategory.minus(user?.dateLocked, new Date())
                    if(timeDiff.seconds < 0) {
                        user?.dateLocked = null
                        user?.accountLocked = false
                        intent?intent.intents=numberIntents.toInteger():null
                    }
                }
                if(user && user.accountLocked){
                    new Logs( "Crear token", "La cuenta ya esta bloqueada", logId, "ERROR", false, [ username:username ] )
                    Utils.logger(logId, "Crear token", "La cuenta ya esta bloqueada", username)
                    return TypeError.exceededAttempts(logId)
                }
                // intent?intent.uuid=UUID.randomUUID().toString().replaceAll('\\-', ''):intent = new IntentRecovery(user:user, intents:numberIntents.toInteger())
                if (!intent) {
                    intent = new IntentRecovery(user:user, intents:numberIntents.toInteger())
                } else {
                    intent.uuid=UUID.randomUUID().toString().replaceAll('\\-', '')
                }
                use(TimeCategory) { 
                    intent.dateExpired = new Date() + minExpired.toInteger().minutes
                    intent.intents -= 1
                    intent.used = 'activo'
                    if (intent.intents < 1) {
                        user.accountLocked = true
                        user.dateLocked = new Date() + minExpired.toInteger().minutes 
                    }
                }
                def to = user?user.username:username
                def subject = flag?"Correo de activación de cuenta":"Correo de recuperación de contraseña" 
                def _body = getbody(intent.uuid, flag)
                def dataMail = Utils.sendEmailApi(logId, to, subject , _body, "Pruebas", [:])
                user?user.save(flush:true, failOnError:true):null
                intent.user?intent.save(flush:true, failOnError:true):null
                new Logs( "Crear token", "Token genereado", logId, "INFO", true, [ username:username ] )
                Utils.logger(logId, "Crear token","Token genereado", username )
                return [data: [success: dataMail], status: 200]
            } catch(Exception e) {
                uStatus.setRollbackOnly()
                new Logs( "Crear token", "Error en la solicitud al crear el Token", logId, e, [ : ] )
                Utils.logger(logId, "Crear token", "Error en la solicitud al crear el Token", "f: ${e.getMessage()}")
                return TypeError.internalError( logId )
            }
        }
    }

    def resetPassword(password, uuid, flag = false,process, logId) {
        Users.withTransaction{uStatus->
            try{
                //TODO: Incorporar para la gestión de empleados, y hacer la parte de la activación de la cuenta
                def intent = IntentRecovery.findByUuid(uuid)
                if (flag && !intent){
                    def user = Users.findByUsername(username)
                }
                if(!intent){
                    new Logs( process, "No se encontró el registro", logId, "ERROR", false, [ uuid:uuid ] )
                    Utils.logger(logId, process, "No se encontró el registro", uuid)
                    return TypeError.informationNotFound( logId )
                }
                TimeDuration timeDiff = TimeCategory.minus(intent.dateExpired, new Date())
                if (timeDiff.seconds < 0) {
                    new Logs( process, "El codigo ha expirado", logId, "ERROR", false, [ uuid:uuid ] )
                    Utils.logger(logId, process, "El codigo ha expirado", uuid)
                    return TypeError.excessTime(logId)
                }
                def user = intent.user
                if (!user) {
                    new Logs( process, "No se encontró el registro", logId, "ERROR", false, [ usuer:usuer ] )
                    Utils.logger(logId, process, "No se encontró el registro", usuer)
                    return TypeError.informationNotFound( logId )
                }
                intent.uuid = null
                intent.used = 'inactivo'
                intent.intents = numberIntents.toInteger()
                user.password = password
                user.save(flush:true, failOnError:true)
                intent.save(flush:true, failOnError:true)
                new Logs( process, "Contraseña actualizada", logId, "INFO", true, [ uuid:uuid ] )
                Utils.logger(logId, process,"Contraseña actualizada", uuid )
                return [data: [success: true], status: 200]
            } catch(Exception e) {
                uStatus.setRollbackOnly()
                new Logs( process, "Error en la solicitud al actualizar la contraseña", logId, e, [ : ] )
                Utils.logger(logId, process, "Error en la solicitud al actualizar la contraseña", "f: ${e.getMessage()}")
                return TypeError.internalError( logId )
            }
        }
    }

    def getbody(token, flag){
        String templatePath
        if (!flag) {
            templatePath = "${Utils.grailsApplication.config.files}/templates/recovery.html"
        } else {
            templatePath = "${Utils.grailsApplication.config.files}/templates/activate.html"
        }
        String link = Utils.redirectMailURL(token, flag)
        File file = new File(templatePath)
        if (!file.exists()) {
            throw new FileNotFoundException("El archivo no existe: $templatePath")
        }
        String fileContent = file.text
        return fileContent.replace('#link#', "${link}").replace('\n', '')
    }
}