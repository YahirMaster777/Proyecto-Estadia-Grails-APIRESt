package com.ordenaris.internalControl


import grails.rest.*
import grails.converters.*

class AppsController {
	static responseFormats = ['json', 'xml']
    def AppsService
    
    def save(){
        def data = request.JSON
        def saveAppResponse = AppsService.createApp(data)
        return respond(saveAppResponse.data,status: saveAppResponse.status)
    
    }

	
	
	

	
}
