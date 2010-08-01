

package com.rural

class LoteController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

	def listjson = {
		log.info("INGRESANDO AL METODO listjson DEL CONTROLLER LoteController")
		log.debug("PARAMETROS $params")
		def c = Lote.createCriteria()
		def lotes = c.list(sort:'nombre',order:'asc'){
			sector{
				eq('id', new Long(params.sector_id))
			}
		} 
		render(contentType:"text/json"){
			total lotes.size()
			rows{
				lotes.each{
					row(id:it.id,nombre:it.nombre)
				}
			}
		}
	}

    def listjsonstock = {
		log.info("INGRESANDO AL METODO listjsonstock DEL CONTROLLER LoteController")
		log.debug("PARAMETROS $params")
		
		def stocklotes = Lote.findAll("from Lote l where l.sector.id=:sector_id and l not in (select d.lote from DetalleServicioContratado d where d.ordenReserva.anulada = false and d.ordenReserva.anio=:anio and d.ordenReserva.expo.id=:expo_id) order by nombre",[sector_id:new Long(params.sector_id),anio:new Integer(params.anio),expo_id:new Long(params.expo_id)])		
		
		render(contentType:"text/json"){
			total stocklotes.size()
			rows{
				stocklotes.each{
					row(id:it.id,nombre:it.nombre)
				}
			}
		}    	
    }
    
    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ loteInstanceList: Lote.list( params ), loteInstanceTotal: Lote.count() ]
    }

    def show = {
        def loteInstance = Lote.get( params.id )

        if(!loteInstance) {
            flash.message = "Lote not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ loteInstance : loteInstance ] }
    }

    def delete = {
        def loteInstance = Lote.get( params.id )
        if(loteInstance) {
            try {
                loteInstance.delete(flush:true)
                flash.message = "Lote ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Lote ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "Lote not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def loteInstance = Lote.get( params.id )

        if(!loteInstance) {
            flash.message = "Lote not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ loteInstance : loteInstance ]
        }
    }

    def update = {
        def loteInstance = Lote.get( params.id )
        if(loteInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(loteInstance.version > version) {
                    
                    loteInstance.errors.rejectValue("version", "lote.optimistic.locking.failure", "Another user has updated this Lote while you were editing.")
                    render(view:'edit',model:[loteInstance:loteInstance])
                    return
                }
            }
            loteInstance.properties = params
            if(!loteInstance.hasErrors() && loteInstance.save()) {
                flash.message = "Lote ${params.id} updated"
                redirect(action:show,id:loteInstance.id)
            }
            else {
                render(view:'edit',model:[loteInstance:loteInstance])
            }
        }
        else {
            flash.message = "Lote not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def create = {
        def loteInstance = new Lote()
        loteInstance.properties = params
        return ['loteInstance':loteInstance]
    }

    def save = {
        def loteInstance = new Lote(params)
        if(!loteInstance.hasErrors() && loteInstance.save()) {
            flash.message = "Lote ${loteInstance.id} created"
            redirect(action:show,id:loteInstance.id)
        }
        else {
            render(view:'create',model:[loteInstance:loteInstance])
        }
    }
    
    def savejson = {
    	log.info("INGRESANDO AL METODO savejson DEL CONTROLLER LoteController")
    	log.debug("PARAMETROS: $params")
    	def loteInstance = new Lote(params)
    	if(!loteInstance.hasErrors() && loteInstance.save()){
    		render(contentType:"text/json"){
    			success true
    			loteid loteInstance.id
    		}
    	}else{
    		log.info("Errores de validacion en lote: ")
    		empresaInstance.errors.allErrors.each{
    			log.info("Mensaje: "+it.defaultMessage)
    		}
    	}
    }
    
    def updatejson = {
    	log.info("INGRESANDO AL METODO updatejson DEL CONTROLLER LoteController")
    	log.debug("PARAMETROS: $params")
    	def loteInstance = Lote.get(params.id)
    	loteInstance.properties=params
    	if(!loteInstance.hasErrors()&& loteInstance.save()){
    		log.debug("Lote modificado con exito")
    		render(contentType:"text/json"){
    			success true
    		}
    	}else{
    		log.info("ERROR DE VALIDACION EN LA MODIFICACION DEL LOTE ")
    		log.info(loteInstance.errors.allErrors)
    		render(contentType:"text/json"){
    			success false
    		}
    	}
    }
    
    def deletejson={
    	log.info("INGRESANDO AL METODO deletejson DEL CONTROLLER LoteController")
    	log.debug("PARAMETROS: $params")
    	def loteInstance = Lote.get(params.id)
    	if(loteInstance){
    		loteInstance.delete(flush:true)
    		render(contentType:"text/json"){
    			try{
    				
    				render(contentType:"text/json"){
    					success true
    					msg "El registro se eliminó correctamente"
    				}
    			}catch(org.springframework.dao.DataIntegrityViolationException e) {s
    				render(contentType:"text/json"){
    					success false
    					msg "No se pudo eliminar el Lote porque está siendo referenciado en otro registro"
    				}
    			}
    		}
    	}else{
    		render(contentType:"text/json"){
    			success false
    			msg "Error. Lote no encontrado"
    		}
    	}
    }
}
