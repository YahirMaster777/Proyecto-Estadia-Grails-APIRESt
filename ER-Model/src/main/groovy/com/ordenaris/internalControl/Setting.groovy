package com.ordenaris.internalControl

import grails.web.context.ServletContextHolder as SCH
import javax.servlet.ServletContext

public class Setting {
    private final static ServletContext servletContext = SCH.servletContext

    public static String NUMBER_RECOVERY_ATTEMPTS= "NUMBER_OF_RECOVERY_ATTEMPTS";
    public static String MINUTES_VALIDITY_CODE= "MINUTES_OF_VALIDITY_CODE";
    public static String MINUTES_ACCOUNT_LOKED= "MINUTES_OF_ACCOUNT_LOKED";

    public static get(identifier) {
        return servletContext["setting"]."$identifier"
    }

    public static set(listSetting, logId) {
        try {
            servletContext.setAttribute("setting",listSetting)
            new Logs("Asignar datos parametrizables", "Se han guardado los datos a la variable global", logId, "INFO", true, [setting:listSetting])
            Utils.logger(logId,"Asignar datos parametrizables","Se han guardado los datos a la variable global", "setting:$listSetting")
        } catch (e) {
            new Logs("Asignar datos parametrizables", "Error al enviar los datos a la variable global", null, e, [:])
            Utils.logger(logId, "Asignar datos parametrizables", "Error al enviar los datos a la variable global", "f: ${e.message}")
            TypeError.internalError(logId)
        }
    }
}