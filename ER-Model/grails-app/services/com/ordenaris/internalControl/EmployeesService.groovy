package com.ordenaris.internalControl

import grails.gorm.transactions.Transactional

@Transactional
class EmployeesService {

    def saveEmployees(data, logId) {
        Employees.withTransaction{status ->
            try{
                new Logs("Registrar", "Procesando Solicitud", logId, "INFO", true, [data:data.name])
                Utils.logger(logId,"Registrar","Procesando Solicitud")
                def employee = new Employees()
            }cathc(e){
            
            }
        
        }

    }
}
