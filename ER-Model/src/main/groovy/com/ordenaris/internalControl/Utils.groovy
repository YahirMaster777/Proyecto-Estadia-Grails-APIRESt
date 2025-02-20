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

    public static dataRequired(hashMapData, process, type, logId) {
        for (validData in hashMapData) {
            def key = validData.keySet().first()
            def value = validData.get(key)
            if (!value) {
                new Logs(process, "No se encontró el dato que se quiere ingresar.", logId, "ERROR", false, [type:key])
                logger(logId, process, "No se encontró el dato que se quiere ingresar", key)
                return TypeError.missingParameter(key, logId)
            }
        }
        return [data: [success: true], status: 200]
    }

    public static validFormatUuid(process, name, uuid, logId) {
        if(!uuid.uuidFormat()){
            new Logs( process, "No coincide el formato esperado.", logId, "ERROR", false, [  name:uuid ] )
            logger(logId,process, "No coincide el formato esperado.", "${name:uuid}")
            return TypeError.incorrectFormat( name, "texto de 32 caracteres", logId )
        }
        return [ data: [success: true], status:200]
    }

    public static validFormatParams(params, table, hashMapFields , logId) {
        if (params.page && (!params.page.onlyInt())){
            new Logs( "Páginado ${table}", "No coincide el formato esperado.", logId, "ERROR", false, [ data: params.page ] )
            logger(logId,"Páginado ${table}", "No coincide el formato esperado.", params.page)
            return TypeError.incorrectFormat( "página", "número entero positivo", logId )
        }
        if (params.max && (!params.max.onlyInt())){
            new Logs( "Páginado ${table}", "No coincide el formato esperado.", logId, "ERROR", false, [ data: params.max ] )
            logger(logId,"Páginado ${table}", "No coincide el formato esperado.", params.max)
            return TypeError.incorrectFormat( "máximo", "número entero positivo", logId )
        }
        if (params.sort && (hashMapFields.indexOf(params.sort) < 0)){
            new Logs( "Páginado ${table}", "No coincide el formato esperado.", logId, "ERROR", false, [ data: params.sort ] )
            logger(logId,"Páginado ${table}", "No coincide el formato esperado.", params.sort)
            return TypeError.incorrectFormat("orden", "${hashMapFields}", logId)
        }
        if (params.orderList && (['asc', 'desc'].indexOf(params.orderList.toLowerCase()) < 0)){
            new Logs( "Páginado ${table}", "No coincide el formato esperado.", logId, "ERROR", false, [ data: params.orderList ] )
            logger(logId,"Páginado ${table}", "No coincide el formato esperado.", params.orderList)
            return TypeError.incorrectFormat("orden de lista","asc o desc", logId )
        }
        return [ data: [success: true], status:200]
    }
}