package com.rural
import grails.converters.JSON


class ExposicionController {
	

    def index = { }
    
    def listjson = {
    	log.info("INGRESANDO AL METODO listjson DE ExposicionController")	
    	def exposiciones = Exposicion.list()
    	render(contentType:'text/json'){
    		//success 'true'
    		//total Exposicion.count()
    		rows{
    			exposiciones.each{
    				row(id: it.id,nombre: it.nombre)
    			}
    		}
    	}
    	
    }
    
}
