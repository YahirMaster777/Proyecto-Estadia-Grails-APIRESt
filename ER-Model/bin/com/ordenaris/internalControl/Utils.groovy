package com.ordenaris.internalControl

import grails.util.Holders
import groovyx.net.http.Method
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.ContentType
import static groovyx.net.http.Method.GET
import static groovyx.net.http.Method.POST
import static groovyx.net.http.ContentType.XML
import static groovyx.net.http.ContentType.JSON

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
            // key, value ->
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

    public static sendApiRequest( host, path, data, headersList, method, type, logId ){
        try {
            new Logs( 'Enviar Peticiones HTTP.', 'Envío de petición HTTP', logId, 'INFO', true, [host:host, path:path, data:data, headers:headersList, method:method, type: type] )
            logger( logId, "Enviar Peticiones HTTP.", "Envío de petición HTTP", "host:$host, path:$path, data:$data, headers:$headersList, method:$method, type:$type" )
            def http = new HTTPBuilder( host )
            method = Method."$method"
            def _type = (type == "rest") ? JSON : XML
            http.request( method, _type ) { request ->
                requestContentType = ContentType.JSON
                uri.path = path
                if ( method == GET ) {
                    uri.query = data
                }
                if ( method == POST ) {
                	if( type == "rest" ) body = (data as grails.converters.JSON).toString()
                	if( type == "soap" ){
                		body = data
                        headers.'Content-Type' = 'text/xml'
                        headers.'Accept' = 'text/xml'
                	} 
                }
                headersList.each { k, v ->
                    headers."${k}" = v
                }
                response.success = { resp, reader ->
                    new Logs( "Enviar Peticiones HTTP.", "Respuesta de servidor - success.", logId, resp, reader )
                    logger( logId, "Enviar Peticiones HTTP.", "Respuesta de servidor - success.", "host:$host, path:$path, data:$data, headers:$headersList, method:$method, type:$type", "$reader" )
                    return reader
                }
                response.failure  = { resp, reader ->
                    new Logs( "Enviar Peticiones HTTP.", "Respuesta de servidor - failure.", logId, resp, reader )
                    logger( logId, "Enviar Peticiones HTTP.", "Respuesta de servidor - failure.", "host:$host, path:$path, data:$data, headers:$headersList, method:$method, type:$type", "$reader" )
                    return reader
                }
            }
        }catch(e) {
            new Logs( "Enviar Peticiones HTTP.", "Ha ocurrido un error.", logId, e, [url: host + path, headers: headersList, method: method, type: type] )
            logger( logId, "Enviar Peticiones HTTP.", "Ha ocurrido un error.", e.getMessage() ?: e.cause ?: e,  "url: $host$path, headers: $headersList, method: $method, type:$type")
            return [success:false, code: TypeError.internalError(logId), message: e.getMessage() ?: e.cause ?: TypeError.internalError(logId), fromException: true]
        }
    }

    public static contructorMail(name = "Onefa", typeService, code, user, subject, text, fromMail= "contacto@WikiControl.com",fromName = "WikiControl", campaign, body, tipeTemplate = 0, template = 0, files) {
        return [
            app: [nombre: name],
            tipoServicio: typeService, // 1- Único / 2- Múltiple
            data: [[
                codigo: code,
                valor: user
            ]],
            request: [
                fromMail: fromMail,
                fromName: fromName,
                to: user,
                subject: subject,
                text: text,
                campaign: campaign,
                html: body,
                tipoTemplate: tipeTemplate,
                template: template,
                files: files
            ]
        ]
    }
}