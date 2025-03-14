package com.ordenaris.internalControl

import grails.web.context.ServletContextHolder as SCH
import javax.servlet.ServletContext

public class Setting {
    private final static ServletContext servletContext = SCH.servletContext

    public static String NUMBER_RECOVERY_ATTEMPTS= "NUMBER_OF_RECOVERY_ATTEMPTS";
    public static String MINUTES_VALIDITY_CODE= "MINUTES_OF_VALIDITY_CODE";

    public static get(identifier) {
        return servletContext["setting"]."$identifier"
    }

    public static set(listSetting) {
        try {
            servletContext.setAttribute("setting",listSetting)
        } catch (Exception e) {
            throw new RuntimeException(e)
        }
    }
}