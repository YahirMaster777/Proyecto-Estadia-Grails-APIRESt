package com.ordenaris.internalControl
import java.util.UUID
class Backup {
    Server server
    String uuid = UUID.randomUUID().toString().replaceAll('\\-', '')
    String locationConfig
    String description
    int status
    //en formato {local, nube o servidor}, ruta
    String route

    static constraints = {
        uuid unique: true
    }
    static mapping = {
        version false
    }
}
