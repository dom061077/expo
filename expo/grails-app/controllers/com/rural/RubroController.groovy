

package com.rural

class RubroController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ rubroInstanceList: Rubro.list( params ), rubroInstanceTotal: Rubro.count() ]
    }

    def show = {
        def rubroInstance = Rubro.get( params.id )

        if(!rubroInstance) {
            flash.message = "Rubro not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ rubroInstance : rubroInstance ] }
    }

    def delete = {
        def rubroInstance = Rubro.get( params.id )
        if(rubroInstance) {
            try {
                rubroInstance.delete(flush:true)
                flash.message = "Rubro ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Rubro ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "Rubro not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def rubroInstance = Rubro.get( params.id )

        if(!rubroInstance) {
            flash.message = "Rubro not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ rubroInstance : rubroInstance ]
        }
    }

    def update = {
        def rubroInstance = Rubro.get( params.id )
        if(rubroInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(rubroInstance.version > version) {
                    
                    rubroInstance.errors.rejectValue("version", "rubro.optimistic.locking.failure", "Another user has updated this Rubro while you were editing.")
                    render(view:'edit',model:[rubroInstance:rubroInstance])
                    return
                }
            }
            rubroInstance.properties = params
            if(!rubroInstance.hasErrors() && rubroInstance.save()) {
                flash.message = "Rubro ${params.id} updated"
                redirect(action:show,id:rubroInstance.id)
            }
            else {
                render(view:'edit',model:[rubroInstance:rubroInstance])
            }
        }
        else {
            flash.message = "Rubro not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def create = {
        def rubroInstance = new Rubro()
        rubroInstance.properties = params
        return ['rubroInstance':rubroInstance]
    }

    def save = {
        def rubroInstance = new Rubro(params)
        if(!rubroInstance.hasErrors() && rubroInstance.save()) {
            flash.message = "Rubro ${rubroInstance.id} created"
            redirect(action:show,id:rubroInstance.id)
        }
        else {
            render(view:'create',model:[rubroInstance:rubroInstance])
        }
    }
    //procedures que trabajan con json
    
    def listrubrojson = {
    	log.info("INGRESANDO AL METODO listrubrojson DEL CONTROLLER RubroController")
    	def rubros = Rubro.list()
    	render(contentType:"text/json"){
    		total rubros.size()
    		rows{
    			rubros.each{
    				
    				row(id:it.id,nombreRubro:it.nombreRubro)
    			}
    		}
    	}
    	
    }
    
    def listsubrubrojson = {
        log.info("INGRESANDO AL METODO listsubrubrojson DEL CONTROLLER RubroController")
        log.debug("PARAMETROS ENVIADOS: "+params)
    	def subrubros = SubRubro.createCriteria().list{
    	 		eq('rubro.id',new Long(params.rubroid))
    	}
    	render(contentType:"text/json"){
			total subrubros.size()    		
    		rows{

    			subrubros.each{
    				row(id:it.id,nombreSubrubro:it.nombreSubrubro)
    			}
    		}
    	}
    }
    //----------------------------------Metodos JSON--------------------------
    
    def savejson = {
    	log.info("INGRESANDO AL METODO savejson DEL CONTROLLER RubroController")
    	log.debug("Parámetros: $params")
    	def errorList = []
    	def rubroInstance = new Rubro(params)
    	if(!rubroInstance.hasErrors() && rubroInstance.save()){
    		log.info("LA INSTANCIA DE Rubro SE GUARDO CORRECTAMENTE")
    		render(contentType:"text/json"){
    			success true
    			idRubro rubroInstance.id
    		}
    	}else{
    		log.info("ERROR DE VALIDACION EN INSTANCIA DE Rubro")
    		rubroInstance.errors.allErrors.each{ error ->
    			error.codes.each{
    				//if(g.message(code:it)!=it)
    					errorList.add(g.message(code:it))
    			}
    		}
    		render(contentType:"text/json"){
    			success false
    			errors{
    				errorList.each{
    					title it
    				}
    			}
    		}
    	}
    }
    
    def updatejson = {
    	log.info("INGRESANDO AL METODO updatejson DEL CONTROLLER RubroController")
    	log.debug("Parámetros : $params")
    	def rubroInstance = Rubro.get(params.id)
    	def errorList = []
    	if (rubroInstance){
    		rubroInstance.properties=params
    		if(!rubroInstance.hasErrors() && rubroInstance.save()){
    			log.info("Instancia de Rubro guardada, renderizando json")
    			render(contentType:"text/json"){
    				success true
    			}
    		}else{
    			log.info("Error de validación en instancia de Rubro")
    			rubroInstance.errors.allErrors.each{error->
    				error.codes.each{
    					if(g.message(code:it)!=it)
    						errorList.add(g.message(code:it))
    				}
    			}
    			render(contentType:"text/json"){
    				success false
    				errorList.each{
    					errors(error(title:it))
    				}
    			}
    			
    		}
    	}else{
    		log.info("Rubro no encontrado con id ${params.id}")
    		render(contentType:"text/json"){
    			success false
    			errors{
    				error(title:"Rubro con id ${params.id} no encontrado")
    			}
    		}
    	}
    }
    
    def deletejson = {
    	log.info("INGRESANDO AL METODO deletejson DEL CONTROLLER RubroController")
    	log.debug("Parametros: $params")
    	def rubroInstance = Rubro.get(params.id)
    	if(rubroInstance){
    		try{
    			rubroInstance.delete(flush:true)
    			log.info("RUBRO CON ID $params.id ELIMINADO" )
    			render(contentType:"text/json"){
    				success true
    			}
    		}catch(org.springframework.dao.DataIntegrityViolationException e){
    			log.info("ERROR DE INTEGRIDAD AL TRATAR DE ELIMINAR EL Rubro CON ID $params.id")
    			render(contentType:"text/json"){
    				success false
    				msg "No se puede eliminar el vendedor porque es referenciado en otro datos"
    			}	
    		}
    	}else{
    		log.info("EL RUBRO CON EL ID $params.id NO PUDO SER ENCONTRADO")
    		render(contentType:"text/json"){
    			success false
    			msg "EL RUBRO CON EL ID $params.id NO PUDO SER ENCONTRADO"
    		}
    	}
    }
    
    def showjson = {
    	log.info("INGRSANDO AL METODO showjosn")
    	log.debug("PARAMETROS: $params")
    	def rubroInstance = Rubro.get(params.id)
    	if (rubroInstance){
    		log.info("Instancia de Rubro encontrada, renderizando json")
    		render(contentType:"text/json"){
    			success true
    			data(id:rubroInstance.id,nombreRubro:rubroInstance.nombreRubro)
    		}
    	}else{
    		log.info("Instancia de Rubro con ID: $params.id NO ENCONTRADA")
    		render(contentType:"text/json"){
    			success false
    			msg "Instancia de Rubro con ID: $params.id NO ENCONTRADA"
    		}	
    	}
    	
    }
    
}
