package com.ordenaris.internalControl

import grails.gorm.transactions.Transactional
import groovy.time.TimeCategory
import groovy.time.TimeDuration
import groovyx.net.http.Method
import java.util.UUID
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

@Transactional
class RecoveryService {
    def grailsApplication
    def bodyHtml
    // def globalSettings = servletContext.getAttribute("dataMapGlobal")
    def minExpired = Setting.findSetting(Setting.MINUTES_VALIDITY_CODE)
    def numberIntents = Setting.findSetting(Setting.NUMBER_RECOVERY_ATTEMPTS)
    // def recoveryAttempts = settingsService.getSetting('NUMBER_OF_RECOVERY_ATTEMPTS')
    // println "Número de intentos de recuperación: $recoveryAttempts"

    def createToken(username, flag = false, logId) {
        Users.withTransaction{ uStatus->
            try {
                println "esta en el servicio"
                def user = Users.findByUsername(username)
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
                println "---"*100
                println "--> los minutos de expiración" + minExpired.toInteger()
                use(TimeCategory) { 
                    intent.dateExpired = new Date() + minExpired.toInteger().minutes
                    intent.intents -= 1
                    intent.used = 'Activo'
                    if (intent.intents < 1) {
                        user.accountLocked = true
                        user.dateLocked = new Date() + minExpired.toInteger().minutes 
                    }
                }
                // def headers = [
                //     "ordServicio": 'a5abc9caf1034669bd157643cbdb4536',
                //     "ordCliente": '6597e01c2cd04dd999dee26bdea097f5',
                // ]
                def subjectMail = flag?"Correo de activación de cuenta":"Correo de recuperación de contraseña" 
                println "---"*100
                println "${getbody(intent.uuid, flag)}"
                // def dataMail = Utils.contructorMail("Onefa", 1, "USUARIO", user?user.username:username, "contacto@WikiControl.com", "WikiControl",subjectMail,"", "Pruebas", getbody(intent.uuid, flag), 0, 0, [:] )
                def dataMail = Utils.sendEmailApi(logId, user?user.username:username, subjectMail, "${getbody(intent.uuid, flag)}", "Pruebas", [:])
                println "--> el html"
                println dataMail
                def responserMail = Utils.sendApiRequest("https://notificaciones.ordenaris.com", "/ordenaris/api/public/email/send",dataMail, headers, Method.POST,"rest", logId)
                user?user.save(flush:true, failOnError:true):null
                intent.user?intent.save(flush:true, failOnError:true):null
                new Logs( "Crear token", "Token genereado", logId, "INFO", true, [ username:username ] )
                Utils.logger(logId, "Crear token","Token genereado", username )
                return [data: responserMail, status: 200]
            } catch(Exception e) {
                uStatus.setRollbackOnly()
                new Logs( "Crear token", "Error en la solicitud al crear el Token", logId, e, [ : ] )
                Utils.logger(logId, "Crear token", "Error en la solicitud al crear el Token", "f: ${e.getMessage()}")
                return TypeError.internalError( logId )
            }
        }
    }

    def resetPassword(password, uuid, flag, logId) {
        Users.withTransaction{uStatus->
            try{
                def intent = IntentRecovery.findByUuid(uuid)
                if (flag && !intent){
                    def user = Users.findByUsername(username)
                }
                if(!intent){
                    new Logs( "Recuperar constraseña", "No se encontró el registro", logId, "ERROR", false, [ uuid:uuid ] )
                    Utils.logger(logId, "Recuperar constraseña", "No se encontró el registro", uuid)
                    return TypeError.informationNotFound( logId )
                }
                TimeDuration timeDiff = TimeCategory.minus(intent.dateExpired, new Date())
                if (timeDiff.seconds < 0) {
                    new Logs( "Recuperar constraseña", "El codigo ha expirado", logId, "ERROR", false, [ uuid:uuid ] )
                    Utils.logger(logId, "Recuperar constraseña", "El codigo ha expirado", uuid)
                    return TypeError.excessTime(logId)
                }
                def user = intent.user
                if (!user) {
                    new Logs( "Recuperar constraseña", "No se encontró el registro", logId, "ERROR", false, [ usuer:usuer ] )
                    Utils.logger(logId, "Recuperar constraseña", "No se encontró el registro", usuer)
                    return TypeError.informationNotFound( logId )
                }
                intent.uuid = null
                intent.used = 'Inactivo'
                intent.intents = numberIntents.toInteger()
                user.password = password
                user.save(flush:true, failOnError:true)
                intent.save(flush:true, failOnError:true)
                new Logs( "Recuperar constraseña", "Contraseña actualizada", logId, "INFO", true, [ uuid:uuid ] )
                Utils.logger(logId, "Recuperar constraseña","Contraseña actualizada", uuid )
                return [data: [success: true], status: 200]
            } catch(Exception e) {
                uStatus.setRollbackOnly()
                new Logs( "Recuperar constraseña", "Error en la solicitud al actualizar la contraseña", logId, e, [ : ] )
                Utils.logger(logId, "Recuperar constraseña", "Error en la solicitud al actualizar la contraseña", "f: ${e.getMessage()}")
                return TypeError.internalError( logId )
            }
        }
    }

    def getbody(token, flag){
        String link
        String templatePath
        if (!flag) {
            templatePath = 'grails-app/views/Mails/recovery.html'
            link = Utils.createUrl(token)
        } else {
            templatePath = 'grails-app/views/Mails/activate.html'
            link = Utils.createUrl(token, flag)
        }
        File file = new File(templatePath)
        if (!file.exists()) {
            throw new FileNotFoundException("El archivo no existe: $templatePath")
        }
        Document htmlContent = Jsoup.parse(file, "UTF-8")
        htmlContent.getElementById("link").attr("href", link)
        return htmlContent.html().replaceAll('  ', '').replaceAll('\n','')
    }

    // def createSetting(key, value) {
        
    // }
    // def updateSetting(key, value) {

    // }
    // def refreshSetting() {

    // }
    // def deleteSetting(key) {

    // }
}