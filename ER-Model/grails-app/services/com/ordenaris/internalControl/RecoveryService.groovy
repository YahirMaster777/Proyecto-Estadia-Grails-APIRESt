package com.ordenaris.internalControl

import grails.gorm.transactions.Transactional
import groovy.time.TimeCategory
import groovy.time.TimeDuration
import groovyx.net.http.Method
import java.util.UUID

@Transactional
class RecoveryService {
    
    def createToken(username, minExpired, numberIntents, logId) {
        Users.withTransaction{ uStatus->
            try {
                def user = Users.findByUsername(username)
                def intent = IntentRecovery.findByUser(user)
                if (user.dateLocked) {
                    TimeDuration timeDiff = TimeCategory.minus(user.dateLocked, new Date())
                    if(timeDiff.seconds < 0) {
                        user.dateLocked = null
                        user.accountLocked = false
                        intent?intent.intents=numberIntents:null
                    }
                }
                if(user.accountLocked){
                    new Logs( "Recuperar constraseña", "La cuenta ya esta bloqueada", logId, "ERROR", false, [ username:username ] )
                    Utils.logger(logId, "Recuperar constraseña", "La cuenta ya esta bloqueada", username)
                    return TypeError.exceededAttempts(logId)
                }
                // !intent?intent=new IntentRecovery(user): intent.uuid=UUID.randomUUID().toString().replaceAll('\\-', '')
                if (!intent) {
                    intent = new IntentRecovery(user:user,intents:numberIntents)
                } else {
                    intent.uuid=UUID.randomUUID().toString().replaceAll('\\-', '')
                }
                use(TimeCategory) { 
                    intent.dateExpired = new Date() + minExpired.minutes
                    intent.intents -= 1
                    if (intent.intents < 1) {
                        user.accountLocked = true
                        user.dateLocked = new Date() + minExpired.minutes 
                    }
                }
                intent.used = 'Activo'
                def headers = [
                    "token": intent.uuid,
                    "ordServicio": 'a5abc9caf1034669bd157643cbdb4536',
                    "ordCliente": '6597e01c2cd04dd999dee26bdea097f5'
                ]
                def responserMail = Utils.sendMailExternal("https://notificaciones.ordenaris.com", "/ordenaris/api/public/email/send", headers, mapData(intent.uuid, user.username), Method.POST , logId)
                if (responserMail.status != 200) return respond(responserMail.data, status:responserMail.status)
                intent.save(flush:true, failOnError:true)
                user.save(flush:true, failOnError:true)
                new Logs( "Recuperar constraseña", "Token genereado", logId, "INFO", true, [ username:username ] )
                Utils.logger(logId, "Recuperar constraseña","Token genereado", username )
                return [data: [success: true, data: [uuid: intent.uuid]], status: 200]
            } catch(Exception e) {
                uStatus.setRollbackOnly()
                new Logs( "Recuperar constraseña", "Error en la solicitud al crear el Token", logId, e, [ : ] )
                Utils.logger(logId, "Recuperar constraseña", "Error en la solicitud al crear el Token", "f: ${e.getMessage()}")
                return TypeError.internalError( logId )
            }
        }
    }

    def mapData(token, user) {
        return [
            app: [
                nombre: "onefa"
            ],
            tipoServicio: 1, // 1- Único / 2- Múltiple
            data: [
                [
                    codigo: "USUARIO",
                    valor: user
                ]
            ],
            request: [
                fromMail: "contacto@onefa.com",
                fromName: "ONEFA",
                to: user,
                subject: "Correo de prueba ${token}",
                text: "esto es un texto",
                campaign: "Pruebas",
                html: "<html><body><h1>Test de Correo Wiki</h1><p>Hola ${user}, este es un correo de prueba.</p></body></html>",
                tipoTemplate: 0,
                template: 0,
            ]
        ]
    }

    def resetPassword(password, uuid, numberIntents, logId) {
        Users.withTransaction{uStatus->
            try{
                def intent = IntentRecovery.findByUuid(uuid)
                if(!intent){
                    new Logs( "Recuperar constraseña", "No se encontró el registro", logId, "ERROR", false, [ uuid:uuid ] )
                    Utils.logger(logId, "Recuperar constraseña", "No se encontró el registro", uuid)
                    return TypeError.informationNotFound( logId )
                }
                TimeDuration timeDiff = TimeCategory.minus(intent.dateExpired, new Date()) //intent.dateExpired - new Date()
                if (timeDiff.seconds < 0) {
                    new Logs( "Recuperar constraseña", "El codigo ha expirado", logId, "ERROR", false, [ uuid:uuid ] )
                    Utils.logger(logId, "Recuperar constraseña", "El codigo ha expirado", uuid)
                    return TypeError.excessTime(logId)
                }
                def user = intent.user
                if (!user) {
                    new Logs( "Recuperar constraseña", "No se encontró el registro", logId, "ERROR", false, [ uuid:uuid ] )
                    Utils.logger(logId, "Recuperar constraseña", "No se encontró el registro", uuid)
                    return TypeError.informationNotFound( logId )
                }
                intent.uuid = ''
                intent.used = 'Inactivo'
                intent.intents = numberIntents
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
}
