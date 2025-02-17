package com.ordenaris.internalControl

class ListUpdates {
    ServersApps conection
    Date dateLastDeploy
    String description
    static constraints = {
        description maxSize:150
    }
    static mapping = {
        version false
    }
}
