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
    def minAccountLock = Setting.get(Setting.MINUTES_ACCOUNT_LOKED)
    def numberIntents = Setting.get(Setting.NUMBER_RECOVERY_ATTEMPTS)

    def createToken(username, logId, flag = false) {
        Users.withTransaction{ uStatus->
            try {
                def user = Users.findByUsername(username)
                if (!user) {return [data: [success: dataMail], status: 200]}
                def intent = IntentRecovery.findByUser(user)
                if (user.dateLocked) {
                    TimeDuration timeDiff = TimeCategory.minus(user.dateLocked, new Date())
                    if(timeDiff.seconds < 0) {
                        user.dateLocked = null
                        user.accountLocked = false
                        // intent?intent.intents=numberIntents.toInteger():null
                        intent?.intents=numberIntents.toInteger()
                    }
                }
                if(user.accountLocked){
                    new Logs( "Crear token", "La cuenta ya esta bloqueada", logId, "ERROR", false, [ username:username ] )
                    Utils.logger(logId, "Crear token", "La cuenta ya esta bloqueada", username)
                    return TypeError.exceededAttempts(logId)
                }
                if (!intent) {
                    intent = new IntentRecovery(user:user, intents:numberIntents.toInteger())
                } else {
                    intent.uuid=UUID.randomUUID().toString().replaceAll('\\-', '')
                }
                use(TimeCategory) { 
                    intent.dateExpired = new Date() + minExpired.toInteger().minutes
                    intent.intents -= 1
                    intent.used = Constants.STATUS_ACTIVE
                    if (intent.intents < 1) {
                        user.accountLocked = true
                        user.dateLocked = new Date() + minAccountLock.toInteger().minutes 
                    }
                }
                def to = user?user.username:username
                def subject = flag?"Correo de activación de cuenta":"Correo de recuperación de contraseña" 
                def _body = getbody(intent.uuid, flag)
                def dataMail = Utils.sendEmailApi(logId, to, subject , _body, "Pruebas", [:])
                user.save(flush:true, failOnError:true)
                intent.save(flush:true, failOnError:true)
                new Logs( "Crear token", "Token genereado", logId, "INFO", true, [ username:username ] )
                Utils.logger(logId, "Crear token","Token genereado", username )
                return [data: [success: dataMail], status: 200]
            } catch(e) {
                uStatus.setRollbackOnly()
                new Logs( "Crear token", "Error en la solicitud al crear el Token", logId, e, [ : ] )
                Utils.logger(logId, "Crear token", "Error en la solicitud al crear el Token", "f: ${e.getMessage()}")
                return TypeError.internalError( logId )
            }
        }
    }

    def resetPassword(password, params, logId) {
        Users.withTransaction{uStatus->
            try{
                new Logs( "Resetear contraseña", "Procesando Solicitud", logId, "INFO", true, [ contraseña:password, token:params.uuid ] )
                Utils.logger(logId, "Resetear contraseña","Procesando Solicitud", "contraseña:$password, token:$params.uuid" )
                def intent = IntentRecovery.findByUuidAndStatus(params.uuid, Constants.STATUS_ACTIVE)
                if(!intent){
                    new Logs( "Resetear contraseña", "No se encontró el registro", logId, "ERROR", false, [ token:params.uuid ] )
                    Utils.logger(logId, "Resetear contraseña", "No se encontró el registro", "token:$params.uuid")
                    return TypeError.informationNotFound( logId )
                }
                TimeDuration timeDiff = TimeCategory.minus(intent.dateExpired, new Date())
                if (timeDiff.seconds < 0) {
                    new Logs( "Resetear contraseña", "El codigo ha expirado", logId, "ERROR", false, [ token:params.uuid ] )
                    Utils.logger(logId, "Resetear contraseña", "El codigo ha expirado", "token:$params.uuid")
                    return TypeError.excessTime(logId)
                }
                def user = intent.user
                if (!user) {
                    new Logs( "Resetear contraseña", "No se encontró el registro", logId, "ERROR", false, [ usuario:user ] )
                    Utils.logger(logId, "Resetear contraseña", "No se encontró el registro", "usuario:$user")
                    return TypeError.informationNotFound( logId )
                }
                intent.uuid = ""
                intent.used = Constants.STATUS_INACTIVE
                intent.intents = numberIntents.toInteger()
                user.password = password
                user.save(flush:true, failOnError:true)
                intent.save(flush:true, failOnError:true)
                new Logs( "Resetear contraseña", "Contraseña actualizada", logId, "INFO", true, [ token:params.uuid ] )
                Utils.logger(logId, "Resetear contraseña","Contraseña actualizada", "token:$params.uuid" )
                return [data: [success: true], status: 200]
            } catch(e) {
                uStatus.setRollbackOnly()
                new Logs( "Resetear contraseña", "Error en la solicitud al actualizar la contraseña", logId, e, [ contraseña:password, token:params.uuid ]  )
                Utils.logger(logId, "Resetear contraseña", "Error en la solicitud al actualizar la contraseña", "f: ${e.getMessage()}")
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