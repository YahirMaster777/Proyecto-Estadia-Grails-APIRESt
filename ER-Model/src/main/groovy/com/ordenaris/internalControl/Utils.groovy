package com.ordenaris.internalControl

import grails.util.Holders
import groovyx.net.http.Method
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.ContentType
import static groovyx.net.http.ContentType.XML
import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.GET
import static groovyx.net.http.Method.POST

public class Utils {
	private static grailsApplication = Holders.grailsApplication
	private final static String app = grailsApplication.config.appName
	public static logger( logId, process, description, info = null, res = null ){
        def log = "${new Date().log()} | $logId | $app | $process | $description"
        if( info ) log += " | $info"
        if( res ) log += " | $res"
        println log
    }

    public static Boolean validateAccessProject( wikiService ){
        return wikiService == grailsApplication.config.id
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
    
    public static sendEmailApi( logId, to, subject, body, campaign, files=[:]){
        try{
            new Logs( "Envío de correo", "Realiza una petición al API de envío de correos", logId, 'INFO', true, [ correo: to, campaign: campaign ])
            logger( logId, "Envío de correo", "Realiza una petición al API de envío de correos", "correo: $to, campaign: $campaign" )
            def nRequest = [
                app: [nombre: grailsApplication.config.apiMail.name],
                tipoServicio: 1,
                data: [],
                request: [
                    fromMail: grailsApplication.config.apiMail.EMAIL_PROJECT,
                    fromName: grailsApplication.config.apiMail.EMAIL_NAME,
                    to: to,
                    subject: subject,
                    campaign: campaign,
                    text: "",
                    html: body,
                    tipoTemplate: 0,
                    template: 0,
                    files: files
                ]
            ]
            def headers = [
                'ordServicio': grailsApplication.config.apiMail.external.ordServicio,
                'ordCliente': grailsApplication.config.apiMail.external.ordCliente
            ]
            def responseApi = sendApiRequest( grailsApplication.config.apiMail.external.url, "/ordenaris/api/public/email/send", nRequest, headers, Method.POST, "rest", logId )
            logger( logId, "Envío de correo", "Respuesta del envío de correo", "correo: $to, campaign: $campaign", "response: $responseApi" )
            new Logs( "Envío de correo", "Respuesta del envío de correo", logId, 'INFO', true, [response: responseApi as HashMap, correo: to, campaign: campaign ] )
            return responseApi.success
        }catch(e){
            logger( logId, "Envío de correo", "Algo salió mal al intentar enviar el correo.", "Algo salió mal al enviar el correo." ,e.getMessage() )
            new Logs( "Envío de correo ", "Algo salió mal al intentar enviar el correo.", logId, e, [ correo: to, campaign: campaign ] )
            return false
        }
    }

    public static separateUrl( _url, logId ){
        try{
            new Logs( "Separación URL", "Inicio de separación de URL", logId, 'INFO', true, [url: _url] )
            logger( logId, "Separación URL", "Inicio de separación de URL", "url:$_url" )
            URL url = new URL( _url )
            def base = "${url.protocol}://${url.host}"   
            if(url.getPort() && url.getPort() > 0) base = base + ":${url.port}"
            return [
                host: base,
                path: url.getPath(),
            ]
        }catch(e) {
            new Logs( "Separación URL", "Error al separar la URL", logId, e, [url: _url] )
            logger( logId, "Separación URL", "Error al separar la URL", e.getMessage() )
            return null
        }
    }

    public static dataRequired(hashMapData, process, logId) {
        for (data in hashMapData) { 
            def key = data.keySet().first()
            def value = data.get(key)
            if (!value) {
                new Logs(process, "Es necesario enviar el dato", logId, "ERROR", false, [key:value])
                logger(logId, process, "Es necesario enviar el dato", key)
                return TypeError.missingParameter(key, logId)
            }
        }
        return [data: [success: true], status: 200]
    }

    public static validFormatUuid(process, name, uuid, logId) {
        if(!uuid.isUuid()){
            new Logs( process, "No coincide el formato esperado", logId, "ERROR", false, [  uuid:uuid ] )
            logger(logId,process, "No coincide el formato esperado", uuid)
            return TypeError.incorrectFormat( name, "un texto de 32 caracteres", logId )
        }
        return [ data: [success: true], status:200]
    }

    public static validPaginationFormat(params, table, hashMapFields , logId) {
        if (params.page && (!params.page.isInt())){
            new Logs( "Páginado ${table}", "No coincide el formato esperado", logId, "ERROR", false, [ page: params.page ] )
            logger(logId,"Páginado ${table}", "No coincide el formato esperado", "Página: ${params.page}")
            return TypeError.incorrectFormat( "página", "número entero positivo", logId )
        }
        if (params.max && (!params.max.isInt())){
            new Logs( "Páginado ${table}", "No coincide el formato esperado", logId, "ERROR", false, [ max: params.max ] )
            logger(logId,"Páginado ${table}", "No coincide el formato esperado","Máximo: ${params.max}")
            return TypeError.incorrectFormat( "máximo", "número entero positivo", logId )
        }
        if (params.order && ([Constants.ASCENDING, Constants.DESCENDANT].indexOf(params.order.toLowerCase()) < 0)){
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

    public static redirectMailURL(token, flag = null){
        if (!flag) {
            return "${grailsApplication.config.apiMail.link}/auth/password-recovery?token=${token}"
        }
        return "${grailsApplication.config.apiMail.link}/auth/activate-account?token=${token}&flag=${flag}"    
    }
}