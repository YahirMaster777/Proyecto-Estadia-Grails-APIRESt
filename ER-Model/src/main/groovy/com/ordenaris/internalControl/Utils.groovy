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
                new Logs("Validación del ${type} de ${table}", "No se encontró el dato que se quiere ingresar.", logId, "ERROR", false, [type:key])
                logger(logId, "Validación del ${type} de ${table}", "No se encontró el dato que se quiere ingresar", key)
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

    public static validFormatParams(params, table, hashMapFields , logId) {
        new Logs( "Páginado ${table}", "Validar parametros de: ${table}", logId, "INFO", true, [ : ] )
        logger(logId, "Páginado ${table}", "Validar parametros de: ${table}")
        def validDataExist = [
            ['página': params.page],
            ['orden': params.sort],
            ['orden de lista': params.orderList]
        ]
        def isArrayExist = validArrayExist(validDataExist, table, "parametro", logId)
        if(isArrayExist.status != 200) return isArrayExist
        if (!params.page.onlyInt()){
            new Logs( "Páginado ${table}", "No coincide el formato esperado.", logId, "ERROR", false, [ data: params.page ] )
            logger(logId,"Páginado ${table}", "No coincide el formato esperado.", params.page)
            return TypeError.incorrectFormat( "página", "número entero positivo", logId )
        }
        if (hashMapFields.indexOf(params.sort) < 0){
            new Logs( "Páginado ${table}", "No coincide el formato esperado.", logId, "ERROR", false, [ data: params.sort ] )
            logger(logId,"Páginado ${table}", "No coincide el formato esperado.", params.sort)
            return TypeError.incorrectFormat("orden", "${hashMapFields}", logId)
        }
        if (['asc', 'desc'].indexOf(params.orderList) < 0){
            new Logs( "Páginado ${table}", "No coincide el formato esperado.", logId, "ERROR", false, [ data: params.orderList ] )
            logger(logId,"Páginado ${table}", "No coincide el formato esperado.", params.orderList)
            return TypeError.incorrectFormat("orden de lista","asc o desc", logId )
        } 
        new Logs( "Páginado ${table}", "Parametros validados de: ${table}", logId, "INFO", true, [ : ] )
        logger(logId,"Páginado ${table}", "Parametros validados de: ${table}")
        return [ data: [success: true], status:200]
    }
}