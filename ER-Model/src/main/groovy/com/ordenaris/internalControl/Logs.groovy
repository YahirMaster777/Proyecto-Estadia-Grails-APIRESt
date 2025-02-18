package com.ordenaris.internalControl

import grails.util.Holders
import groovyx.net.http.*

import groovy.json.internal.LazyMap
import org.apache.catalina.connector.RequestFacade
/**
    .:.:.:.:.:.:.:.:.:.:.:.:.:.:.:.:TIPOS LOG:.:.:.:.:.:.:.:.:.:.:.:.:.:.:.:.
    -ACCESS: Inicio o cierre de sesión.
    -DEBUG: Proceso de un flujo hasta que termina.
    -ERROR: La interrupción de un flujo por un agente extraordinario.
    -INFO Sólo se require vizualizar un dato en el proceso
**/

public class Logs {
    def grailsApplication = Holders.grailsApplication

    def APP = grailsApplication.config.appName //Variable de configuración en el  aplication.yml
    def VERSION = grailsApplication.metadata['info.app.version']

    private static String tipo = 'DEBUG'
    private static String estatus = 'ok'
    public static String id = ""
    
    //*Log inicial
    public Logs( String servicio, String descripcion, RequestFacade request, String id = null ){
        tipo = 'DEBUG'
        estatus = 'ok'
        id = id?:UUID.randomUUID().toString().replaceAll( '\\-','' )
        def extra = buildDataInicio( request )
        buildLog( servicio, descripcion, id, extra )
    }
    //* Constructor para los mensajes de error.
    public Logs( String servicio, String descripcion, String id, Exception error, HashMap info = null ){
            this.tipo = 'ERROR'
            this.estatus = 'fail'
            def extra
            if( error.getClass().getPackage().getName().equals("java.net") ){
                info.error = buildDataError( error )
                extra = info
            }else{
                extra = buildDataError( error )
            }
            buildLog( servicio, descripcion, id, extra )
    }

    //*Contructor para los mensajes de request.
    public Logs( String servicio, String descripcion, String id, HttpResponseDecorator request, LazyMap response ){
        def extra = buildDataRequest( request, response )
        if( extra.status != 200 ) this.estatus = 'fail'
        buildLog( servicio, descripcion, id, extra )
    }
    
    //*Constructor de logs custom con mensaje
    public Logs( String servicio, String descripcion, String id, String tipo, Boolean estatus, String mensaje ){
        this.tipo = tipo
        this.estatus = estatus?'ok':'fail'
        buildLog( servicio, descripcion, id, mensaje )
    }

    //*Constructor de logs custom con data
    public Logs( String servicio, String descripcion, String id, String tipo, Boolean estatus, HashMap info ){
        this.tipo = tipo
        this.estatus = estatus?'ok':'fail'
        buildLog( servicio, descripcion, id, info )
    }

    def buildLog( servicio, descripcion, id, extra = null ){
        def logInfo = [
            timestamp: new Date().getTime(),
            id: id,
            app: APP,
            version: VERSION,
            tipo: this.tipo,
            estatus: this.estatus,
            servicio: servicio,
            descripcion: descripcion
        ]
        if( extra instanceof String ){
            logInfo.mensaje = extra 
        }else{
            logInfo.info = extra 
        }
        saveLogFile( logInfo.toPrettyString() )
        return this.id = logInfo.id
    }

    def getId(){
        return this.id
    }

    def buildDataInicio( request ){
        def newIP = request.getHeader('X-Forwarded-For')
        if( !newIP ){
            newIP = request.getHeader('Client-IP')
            if( !newIP ){
                newIP = request.getRemoteAddr()
            }
        }

        def headers = [:]
        request.getHeaderNames().each{ header->
            headers."${header}" = request.getHeader( header )
        }

        return [
            origin: newIP,
            agent: request.getHeader('User-Agent'),
            metodo: request.getMethod(),
            referHTTP: request.getRequestURL(),
            headers: headers,
            body: request.JSON,
            localizacion: [
                latitude: '',
                longitude: ''
            ]
        ]
    }

    def buildDataRequest( request, response ){
        return [
            // referHTTP: request.getRequestURL(),
            // method: request.method,
            status: request.getStatus(),
            headers: request.getAllHeaders(),
            responseBody: response,
            time: '',
            size: '',
        ]
    }

    def buildDataError( exception ){
        return [
            clase: exception.class.toString(),
            mensaje: exception.getMessage() ?: exception.cause ?:exception
        ]
    }

    def buildDataLogin( login ){
        def info = [ user: login.principal]
        return info
    }

    def saveLogFile( logInfo ){
        try{
            File file = new File("${grailsApplication.config.rootLogs}/InternalControl.txt")
            if( !file.exists() ) file.createNewFile()
            FileWriter writer = new FileWriter( file, true )
            writer.write( "${logInfo}\n" )
            writer.close()
        }catch(e){  
            println "${new Date().format('yyyy-MM-dd HH:mm:ss :::=>')} | Esim | Logs | Save Log File | Archivo de Log | ${e.getMessage()} "
        }
    }
}