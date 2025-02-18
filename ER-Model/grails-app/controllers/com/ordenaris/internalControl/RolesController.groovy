package com.ordenaris.internalControl


import grails.rest.*
import grails.converters.*

class RolesController {
	static responseFormats = ['json', 'xml']
	
    def create() { 
        def data = request.JSON
        // Seguimiento de la información
        println "${new Date()} -> Gestion de Usiario -> Inicio de Solicitud de creación -> data: ${data}"
        if(!data.username) return respond( [success: false, message: "Parametro username requerido"])
        if(!data.password) return respond( [success: false, message: "Parametro password requerido"])
        if(!data.role) return respond( [success: false, message: "Parametro role requerido"])
        def responseService= UsersService.manageUser(data, params.username)
        respond(responseService)
    }

    def  update() {

    }

    def read() {

    }
    def delete() {

    }
    def list() {

    }
    def all() {
        
    }
}
