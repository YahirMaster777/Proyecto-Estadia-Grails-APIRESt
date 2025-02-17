package com.ordenaris.internalControl

public class TypeError {

    static HashMap missingParameter(String data, String logId) {
        return [data: [success:false, message: String.format("Es necesario enviar el dato %s.", data), id:logId], status: 400];
    }
    
    static HashMap incorrectFormat(String data, String format, String logId) {
        return [data: [success:false, message: String.format("El dato %s tiene un formato incorrecto. Debe ser %s.", data, format), id:logId], status: 400];
    }

    static HashMap noPermissions(String logId) {
        return [data: [success:false, message: String.format("No cuenta con permisos para acceder a este recurso."), id:logId], status: 401];
    }

    static HashMap informationNotFound(String logId) {
        return [data: [success:false, message: String.format("No se encontró la información solicitada."), id:logId], status: 412];
    }

    static HashMap internalError(String logId) {
        return [data: [success:false, message: String.format("Se ha producido un error interno. Inténtelo de nuevo más tarde."), id:logId], status: 500];
    }

    static HashMap invalidData(String data, String logId) {
        return [data: [success:false, message: String.format("El dato %s es inválido.", data), id:logId], status: 403];
    }

    static HashMap apiLoginFailed(String logId) {
        return [data: [success:false, message: String.format("No se obtuvieron los permisos para acceder a este recurso."), id:logId], status: 407];
    }

    static HashMap externalApiFailure(String message, String logId) {
        return [data: [success:false, message: message ? message : "Se ha producido un error de conexión.", id:logId], status: 424];
    }

    static HashMap exceededAttempts( String logId, Integer horas = null) {
        if(horas==null){
            return [data: [success:false, message: String.format("Ha superado el máximo de intentos permitidos. Inténtalo más tarde."), id:logId], status: 429];
        }
        return [data: [success:false, message: String.format("Ha superado el máximo de intentos permitidos. Inténtalo en: %s horas", horas ), id:logId], status: 429];
    }

    static HashMap excessTime(String logId) {
        return [data: [success:false, message: String.format("El código que has introducido ha expirado."), id:logId], status: 410];
    }

    static HashMap existingRegister(String logId) {
        return [data: [success:false, message: String.format("No es posible procesar la solicitud, por favor utilice valores diferentes."), id:logId], status: 409];
    }

}
