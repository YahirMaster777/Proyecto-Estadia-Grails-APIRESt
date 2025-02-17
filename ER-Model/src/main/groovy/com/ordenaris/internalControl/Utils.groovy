package com.ordenaris.internalControl

import grails.util.Holders
public class Utils {
	private static grailsApplication = Holders.grailsApplication
	private final static String app = grailsApplication.config.appName
	public static logger( logId, process, description, info = null, res = null ){
        def log = "${new Date().log()} | $logId | $app | $process | $description"
        if( info ) log += " | $info"
        if( res ) log += " | $res"
        println log
    }

    public static validArrayExist(validDataExists, table, type, logId) {
        for (validData in validDataExists) {
            def key = validData.keySet().first()
            def value = validData.get(key)
            if (!value) {
                new Logs("Validación del ${type} del ${table}", "No se encontró el dato que se quiere ingresar.", logId, "ERROR", false, [:])
                logger(logId, "Validación del ${type} del ${table}", "No se encontró el dato que se quiere ingresar")
                return TypeError.missingParameter(key, logId)
            }
        }
        new Logs("Validación de ${type} del ${table}", "Control de la información de los ${type}", logId, "INFO", true, [:])
        logger(logId, "Validación de ${type} del ${table}", "Control de la información de los ${type}.")
        return [data: [success: true], status: 200]
    }

    public static validFormatDataUuid(uuid, logId) {
        if(!uuid.uuidFormat()){
            new Logs( "Validación del parametro uuid", "El parametro uuid no coincide con la longitud que se quiere ingresar.", logId, "ERROR", false, [  uuid: uuid ] )
            logger(logId,"Validación del parametro uuid", "El parametro uuid no coincide con la longitud que se quiere ingresar.", uuid)
            return TypeError.incorrectFormat( "uuid", "texto de 32 caracteres", logId )
        }
        new Logs( "Validación del parametro uuid", "Control de la información del uuid", logId, "INFO", true, [  uuid: uuid ] )
        logger(logId,"Validación del parametro uuid", "El parametro uuid paso.")
        return [ data: [success: true], status:200]
    }
}