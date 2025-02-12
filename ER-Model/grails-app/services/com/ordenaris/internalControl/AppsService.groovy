package com.ordenaris.internalControl

import grails.gorm.transactions.Transactional

@Transactional
class AppsService {

    def createApp(data){
        Apps.withTransaction{appStatus-> 
            try{
                def aplication = new Apps(data)
                aplication.save(failOnError:true, flush:true)
                return [data:[success:true,message:"Se registro"]]
            }catch(e){
                println("Error")
                appStatus.setRollbackOnly()
                println(${e.getMessage()})
            }
        } 
    }
    
}
