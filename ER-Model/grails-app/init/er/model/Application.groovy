package er.model

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean
import org.springframework.context.EnvironmentAware
import org.springframework.core.env.Environment
import org.springframework.core.env.MapPropertySource
import org.springframework.core.env.PropertiesPropertySource
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource

class Application extends GrailsAutoConfiguration implements EnvironmentAware {
    static void main(String[] args) {
        GrailsApp.run(Application, args)
    }

    @Override
    void setEnvironment(Environment environment) {
        def configBase
        if( environment.activeProfiles[0] == 'development' ){
            // configBase =  new File( "C:/Users/ord-back7/Desktop/Archivo Externo/config.groovy" ) // ruta emilio
            configBase =  new File( "C:/Users/marvi/Desktop/Archivo externo/config.groovy" ) // ruta emilio
        }
        if( environment.activeProfiles[0] == 'stage_qa' ){
            configBase =  new File( "/mnt/config/.core-pospago.groovy" ) //TODO: Pendiente de cambio
        }
        if( environment.activeProfiles[0] == 'production' ){
            configBase =  new File( "/mnt/config/.core-pospago.groovy" ) //TODO: Pendiente de cambio
        }
        if(configBase.exists()) {
            println "${new Date().format('yyyy-MM-dd HH:mm:ss')} | Wiki Internal Control Connection | Loading configuration. | Success | path: ${configBase.absolutePath}"
            def config = new ConfigSlurper().parse(configBase.toURL())
            environment.propertySources.addFirst(new MapPropertySource("externalGroovyConfig", config))
        } else {
            println "${new Date().format('yyyy-MM-dd HH:mm:ss')} | Wiki Internal Control Connection  | Loading configuration. | Fail | path: ${configBase.absolutePath}"
        }
    }
}