package com.ordenaris.internalControl

import grails.web.context.ServletContextHolder as SCH
import javax.servlet.ServletContext

public class Setting {
    private final static ServletContext servletContext = SCH.servletContext

    public static String NUMBER_RECOVERY_ATTEMPTS= "NUMBER_OF_RECOVERY_ATTEMPTS";
    public static String MINUTES_VALIDITY_CODE= "MINUTES_OF_VALIDITY_CODE";

    public static findSetting(identifier) {
        println "Buscando " + identifier
        return servletContext["setting"]."$identifier"
    }

    public static refreshData(logId = null) {
        try {
            new Logs("Refrscar datos parametrizables", "Procesando Solicitud", logId, "INFO", true, [:])
            Utils.logger(logId, "Refrscar datos parametrizables", "Procesando Solicitud")
            def dataMapGlobal = [:]
            Settings.list().each { setting ->
                dataMapGlobal[setting.identifier] = setting.data
            }
            // servletContext.setAttribute("setting",[:])
            servletContext.setAttribute("setting",dataMapGlobal)
            new Logs("Refrscar datos parametrizables", "Valores actualizados", logId, "INFO", true, [:])
            Utils.logger(logId, "Refrscar datos parametrizables", "Valores actualizados")
            return [data: [success: true], status: 200]
        } catch (Exception e) {
            new Logs("Refrscar datos parametrizables", "Error en la solicitud al refrescar los datos", null, e, [:])
            Utils.logger(logId, "Refrscar datos parametrizables", "Error en la solicitud al refrescar los datos", "f: ${e.message}")
            throw new RuntimeException(e)
        }
    }   
}