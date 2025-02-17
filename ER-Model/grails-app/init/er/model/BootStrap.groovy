package er.model

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import groovy.json.JsonBuilder;

class BootStrap {
    def init = { servletContext ->
        String.metaClass.validFormatDataHour = {
            def horaCodeExpression = '([0-1][1-9]|[2][0-3])(:)([0-5][0-9])$'
            def pattern = Pattern.compile(horaCodeExpression) 
            def matcher = pattern.matcher( delegate ) 
            return matcher.matches()  
        }
        String.metaClass.onlyInt = {
            def pageExpression = '^\\d+$'
            def pattern = Pattern.compile(pageExpression)
            def matcher = pattern.matcher(delegate)
            return matcher.matches()
        }
        String.metaClass.phoneNumber = {
        def pageExpression = '^\\d{10}$'
            def pattern = Pattern.compile(pageExpression)
            def matcher = pattern.matcher(delegate)
            return matcher.matches()
        }
        String.metaClass.uuidFormat = {
            def pageExpression = '^[a-f0-9]{32}$'
            def pattern = Pattern.compile(pageExpression)
            def matcher = pattern.matcher(delegate)
            return matcher.matches()
        }
        String.metaClass.specialCharacters = {
            def pageExpression = '^[a-zA-Z0-9]+$'
            def pattern = Pattern.compile(pageExpression)
            def matcher = pattern.matcher(delegate)
            return matcher.matches()
        }
        String.metaClass.onlyDouble = {
            def pageExpression = "^[0-9]+(.[0-9]+)?\$"
            def pattern = Pattern.compile(pageExpression)
            def matcher = pattern.matcher(delegate)
            return matcher.matches()
        }
        String.metaClass.validarPathImg = {
            // delegate.matches(/^\/.*\.webp$/)
            def pageExpression = '/^\\/.*\\.webp$/'
            def pattern = Pattern.compile(pageExpression)
            def matcher = pattern.matcher(delegate)
            return matcher.matches()
        }
        Date.metaClass.log = {
            delegate.format("yyyy-MM-dd HH:mm:ss")
        }
        Object.metaClass.toPrettyString = {
            try {
                return new JsonBuilder(delegate).toPrettyString().replaceAll('\n', '').replaceAll('    ', '')
            }catch(e) {
                return '{ERROR-AL-GENERAL-JSON}'
            }
        }
    }
    def destroy = {
    }
}

