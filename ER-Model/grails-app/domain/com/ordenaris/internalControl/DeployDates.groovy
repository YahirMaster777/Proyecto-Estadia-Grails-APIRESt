package com.ordenaris.internalControl

class DeployDates{
    Date dateDeploy = new Date()
    String typeEnvironment
    String version
    
    static belongsTo= [app:Apps]
    
    static mapping = {
        dateDeploy index:"deployDates_dateDeploy_idx"
        typeEnvironment index:"deployDates_typeEnvironment_idx"
        version false
    }
    
    static constraints = {
        typeEnvironment inList:[Constants.STATUS_TESTS,Constants.STATUS_DEVELOPMENT, Constants.STATUS_PRODUCTION]
    }
}
