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
                // TODO hacer la parte para la activación de la cuenta
                if(user.accountLocked){
                    new Logs( "Recuperar constraseña", "La cuenta ya esta bloqueada", logId, "ERROR", false, [ username:username ] )
                    Utils.logger(logId, "Recuperar constraseña", "La cuenta ya esta bloqueada", username)
                    return TypeError.exceededAttempts(logId)
                }
                if(!user.enabled) {
                    user.flag = UUID.randomUUID().toString.replaceAll('\\-', '')
                }
                // !intent?intent=new IntentRecovery(user): intent.uuid=UUID.randomUUID().toString().replaceAll('\\-', '')
                if (!intent) {
                    intent = new IntentRecovery(user:user, intents:numberIntents)
                } else {
                    intent.uuid=UUID.randomUUID().toString().replaceAll('\\-', '')
                }
                use(TimeCategory) { 
                    intent.dateExpired = new Date() + minExpired.minutes
                    intent.intents -= 1
                    intent.used = 'Activo'
                    if (intent.intents < 1) {
                        user.accountLocked = true
                        user.dateLocked = new Date() + minExpired.minutes 
                    }
                }
                def headers = [
                    "ordServicio": 'a5abc9caf1034669bd157643cbdb4536',
                    "ordCliente": '6597e01c2cd04dd999dee26bdea097f5'
                ]
                def responserMail = Utils.sendMailExternal("https://notificaciones.ordenaris.com", "/ordenaris/api/public/email/send", headers, mapData(intent.uuid, user.username, flag), Method.POST , logId)
                // if (responserMail.status != 200) return respond(responserMail.data, status:responserMail.status)
                intent.save(flush:true, failOnError:true)
                user.save(flush:true, failOnError:true)
                new Logs( "Recuperar constraseña", "Token genereado", logId, "INFO", true, [ username:username ] )
                Utils.logger(logId, "Recuperar constraseña","Token genereado", username )
                // return [data: [success: true, data: [uuid: intent.uuid]], status: 200]
                return responserMail
            } catch(Exception e) {
                uStatus.setRollbackOnly()
                new Logs( "Recuperar constraseña", "Error en la solicitud al crear el Token", logId, e, [ : ] )
                Utils.logger(logId, "Recuperar constraseña", "Error en la solicitud al crear el Token", "f: ${e.getMessage()}")
                return TypeError.internalError( logId )
            }
        }
    }

    def mapData(token, user, flag) {
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
                fromMail: "contacto@WikiControl.com",
                fromName: "WikiControl",
                to: user,
                subject: "Correo de prueba ${token}, usuario: ${user}",
                text: "esto es un texto",
                campaign: "Pruebas",
                // recuperación
                // html: "<!DOCTYPE html><html lang='en'><head> <meta charset='UTF-8'> <meta name='viewport' content='width=device-width, initial-scale=1.0'> <title>Pasword Request</title> <style type='text/css'> body{ font-family:'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; } @media (max-width: 640px) { .img-size-logo{ width: 8rem; } .text-title{ font-size: 1.8rem; padding: 4rem 0 1rem; } .text-description{ padding: 0 1rem 0; } .button-class{ font-size: 1rem; } table{ width: 100%; } } @media (min-width: 641px) and (max-width: 1280px) { .text-title{ font-size: 2.2rem; padding: 5rem 0 1rem; } .img-size-logo{ width: 11rem; } .text-description{ font-size: 1.2rem; padding:0 4rem 0; } .button-class{ font-size: 1.5rem; } table{ width: 80%; } } @media (min-width: 1280px) { .img-size-logo { width: 11rem; } .text-title{ font-size: 2.8rem; padding:5rem 0 2rem; } .text-description{ font-size: 1.2rem; padding:0 4rem 0; } .button-class{ font-size: 1.8rem; } table{ width: 50%; } } </style></head><body style='width: 100%;padding: 0; margin: 0;'> <div style='background-color: #7e7e7e;width: 100%; margin: 0 auto;' align='center'> <table style='height: 100vh; background-color: rgb(255, 255, 255);' cellspacing='0' cellpadding='0' > <tr> <td colspan='2' style='padding:3rem 0 0; background-color: white; height: 8%;' align='center'> <img class='img-size-logo' src='https://ordenaris.com/img/logo.png' alt='ordenaris''> </td> </tr> <tr align='center' style='height: 8%;'> <td> <img class='img-size-logo' src='https://innovattia.com/wp-content/uploads/2025/01/IMAGOTIPO_INNOVATTIA2.png' alt='innovattia'> </td> <td> <img class='img-size-logo' src='https://pawerful.com/assets/images/logo-big.png' alt='pawerful'> </td> </tr> <tr> <td colspan='2' align='center' style='height: 0;'> <p class='text-title' style='margin: 0; font-weight: bold; color: #4B4B4B;'>¿Olvidaste tu contraseña?</p> </td> </tr> <tr> <td colspan='2' align='center' style='height: 0;'> <p class='text-description'>Te ha llegado una solicitud de <span style='font-weight: bold;'>cambio de contraseña</span>, para poder actualizarla visita la siguiente dirección: </p> </td> </tr> <tr> <td colspan='2' align='center'> <div> <a href='http://localhost:8080/' class='button-class' style='background-color: #149CC3; color: white;font-weight: 700; padding:15px 20px 15px;border-radius: 1.3rem; border: none; text-decoration: none;'> Actualizar Contraseña </a> </div> </td> </tr> <tr> <td valign='top' colspan='2' align='center' style='font-weight: 100; color: #4B4B4B;'> <p class='text-description'>Si no has solicitado un cambio de contraseña, ignora este correo y repórtalo a: <span style='color: #1F5289;'>user@example.com</span></p> </td> </tr> <tr> <td style='background-color: #4B4B4B; height: 7%; color: white;' colspan='2' align='center'> <p>Copyright© 2022 Ordenaris. Todos los derechos reservados</p> </td> </tr> </table> </div></body></html>",
                // activación
                html: "<!DOCTYPE html><html lang='en'><head> <meta charset='UTF-8'> <meta name='viewport' content='width=device-width, initial-scale=1.0'> <title>User Activation</title> <style type='text/css'> body{ font-family:'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }@media (max-width: 640px) { .img-size-logo{ width: 8rem; } .text-title{ font-size: 1.8rem; padding: 4rem 0 1rem; } .text-description{ padding: 0 1rem 0; } .button-class{ font-size: 1rem; } table{ width: 100%; } } @media (min-width: 641px) and (max-width: 1280px) { .text-title{ font-size: 2.2rem; padding: 5rem 0 1rem; } .img-size-logo{ width: 11rem; } .text-description{ font-size: 1.2rem; padding:0 4rem 0; } .button-class{ font-size: 1.5rem; } table{ width: 80%; } } @media (min-width: 1280px) { .img-size-logo { width: 11rem; } .text-title{ font-size: 2.8rem; padding:5rem 0 2rem; } .text-description{ font-size: 1.2rem; padding:0 4rem 0; } .button-class{ font-size: 1.8rem; } table{ width: 50%; } } </style></head><body style='width: 100%;padding: 0; margin: 0;'> <div style='background-color: #7e7e7e;width: 100%; margin: 0 auto;' align='center'> <table style='height: 100vh; background-color: rgb(255, 255, 255);' cellspacing='0' cellpadding='0' > <tr> <td colspan='2' style='padding:3rem 0 0; background-color: white; height: 8%;' align='center'> <img class='img-size-logo' src='https://ordenaris.com/img/logo.png' alt='ordenaris''> </td> </tr> <tr align='center' style='height: 8%;'> <td> <img class='img-size-logo' src='https://innovattia.com/wp-content/uploads/2025/01/IMAGOTIPO_INNOVATTIA2.png' alt='innovattia'> </td> <td> <img class='img-size-logo' src='https://pawerful.com/assets/images/logo-big.png' alt='pawerful'> </td> </tr> <tr> <td colspan='2' align='center' style='height: 0;'> <p class='text-title' style='margin: 0; font-weight: bold; color: #4B4B4B;'>¡Bienvenido a WikiControl!</p> </td> </tr> <tr> <td colspan='2' align='center' style='height: 0;'> <p class='text-description'>Tu cuenta ha sido dada de alta, por favor continua con el proceso para poder activar tu cuenta y conocer todos los <span style='font-weight: bold;'>servidores y aplicaciones</span> existentes dentro de tu empresa.</p> </td> </tr> <tr> <td colspan='2' align='center'> <div> <a href='http://localhost:8080/${intents.uuid}' class='button-class' style='background-color: #149CC3; color: white;font-weight: 700; padding:15px 20px 15px;border-radius: 1.3rem; border: none; text-decoration: none;'> ¡Activa tu cuenta! </a> </div> </td> </tr> <tr> <td valign='top' colspan='2' align='center' style='font-weight: 100; color: #4B4B4B;'> <p class='text-description'>Si no has solicitado esta activación, ignora este correo y repórtalo a: <span style='color: #1F5289;'>user@example.com</span></p> </td> </tr> <tr> <td style='background-color: #4B4B4B; height: 7%; color: white;' colspan='2' align='center'> <p>Copyright© 2022 Ordenaris. Todos los derechos reservados</p> </td></tr></table></div></body></html>",
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
                    new Logs( "Recuperar constraseña", "No se encontró el registro", logId, "ERROR", false, [ usuer:usuer ] )
                    Utils.logger(logId, "Recuperar constraseña", "No se encontró el registro", usuer)
                    return TypeError.informationNotFound( logId )
                }
                intent.uuid = null
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