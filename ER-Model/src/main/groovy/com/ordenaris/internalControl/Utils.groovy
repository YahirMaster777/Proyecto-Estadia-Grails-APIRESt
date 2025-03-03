package com.ordenaris.internalControl

import grails.util.Holders
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.RESTClient
import groovyx.net.http.Method
import groovy.json.JsonOutput


public class Utils {
	private static grailsApplication = Holders.grailsApplication
	private final static String app = grailsApplication.config.appName
	public static logger( logId, process, description, info = null, res = null ){
        def log = "${new Date().log()} | $logId | $app | $process | $description"
        if( info ) log += " | $info"
        if( res ) log += " | $res"
        println log
    }

    public static dataRequired(hashMapData, process, logId) {
        for (validData in hashMapData) {
            def key = validData.keySet().first()
            def value = validData.get(key)
            if (!value) {
                new Logs(process, "Es necesario enviar el dato", logId, "ERROR", false, [key:value])
                logger(logId, process, "Es necesario enviar el dato", key)
                return TypeError.missingParameter(key, logId)
            }
        }
        return [data: [success: true], status: 200]
    }

    public static validFormatUuid(process, name, uuid, logId) {
        if(!uuid.uuidFormat()){
            new Logs( process, "No coincide el formato esperado", logId, "ERROR", false, [  uuid:uuid ] )
            logger(logId,process, "No coincide el formato esperado", uuid)
            return TypeError.incorrectFormat( name, "un texto de 32 caracteres", logId )
        }
        return [ data: [success: true], status:200]
    }

    public static validFormatParams(params, table, hashMapFields , logId) {
        if (params.page && (!params.page.onlyInt())){
            new Logs( "Páginado ${table}", "No coincide el formato esperado", logId, "ERROR", false, [ page: params.page ] )
            logger(logId,"Páginado ${table}", "No coincide el formato esperado", "Página: ${params.page}")
            return TypeError.incorrectFormat( "página", "número entero positivo", logId )
        }
        if (params.max && (!params.max.onlyInt())){
            new Logs( "Páginado ${table}", "No coincide el formato esperado", logId, "ERROR", false, [ max: params.max ] )
            logger(logId,"Páginado ${table}", "No coincide el formato esperado","Máximo: ${params.max}")
            return TypeError.incorrectFormat( "máximo", "número entero positivo", logId )
        }
        if (params.order && (['asc', 'desc'].indexOf(params.order.toLowerCase()) < 0)){
            new Logs( "Páginado ${table}", "No coincide el formato esperado", logId, "ERROR", false, [ order: params.order ] )
            logger(logId,"Páginado ${table}", "No coincide el formato esperado", "Orden de lista: ${params.order}")
            return TypeError.incorrectFormat("orden de lista","asc o desc", logId )
        }
        if (params.sort && (hashMapFields.indexOf(params.sort) < 0)){
            new Logs( "Páginado ${table}", "No coincide el formato esperado", logId, "ERROR", false, [ sort: params.sort ] )
            logger(logId,"Páginado ${table}", "No coincide el formato esperado", "Orden: ${params.sort}")
            return TypeError.incorrectFormat("orden", "${hashMapFields}", logId)
        }
        return [ data: [success: true], status:200]
    }   

    public static sendMailExternal(String baseUrl, String path, Map requestHeaders=[:], Map _query=[:], method = Method.POST, logId) {
        def result = null
        try {
            def http = new HTTPBuilder(baseUrl)
            http.request(method, ContentType.JSON) { request -> // el request -> tambien es para un get
                uri.path = path
                body = (_query as grails.converters.JSON).toString() //Cuando es un post
                // uri.query = _query // cuando es un get
                headers.'Content-Type' = 'application/json'
                requestHeaders.each { key, value ->
                    headers."${key}" = "${value}"
                }
                response.success = { resp, reader ->
                    result = reader.get(response)
                }
            }
            new Logs( "Recuperar constraseña", "Correo enviado", logId, "INFO", true, [ : ] )
            logger(logId, "Recuperar constraseña","Correo enviado" )
            return [data: [success: true, data:result], status: 200]
        } catch (Exception e) {
            new Logs( "Recuperar constraseña", "Error en la solicitud al enviar el correo", logId, e, [ : ] )
            logger(logId, "Recuperar constraseña", "Error en la solicitud al enviar el correo", "f: ${e.getMessage()}")
            return TypeError.externalApiFailure(result.mensaje,  logId )
        }
    }

    // final externalUrl = "https://notificaciones.ordenaris.com/ordenaris/api/public/email/send"
    // public static sendMailRecovery(token, user) {
    public static sendMailRecovery(name, typeService, code, value, fromMail, fromName, toMailArray, subject, text, campaign, html, typeTemplate, template, filesArray) {
        return [
            app: [nombre: name],
            tipoServicio: typeService, // 1- Único / 2- Múltiple
            data: [[
                codigo: code,
                valor: value
            ]],
            request: [
                fromMail: fromMail,
                fromName: fromName,
                to: [toMailArray],
                subject: subject,
                text: text,
                campaign: campaign,
                html: html,
                tipoTemplate: typeTemplate,
                template: template,
                files: [ filesArray] 
            ]
        ]
    }
}