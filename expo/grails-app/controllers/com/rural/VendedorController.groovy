

package com.rural

class VendedorController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ vendedorInstanceList: Vendedor.list( params ), vendedorInstanceTotal: Vendedor.count() ]
    }
    
    def listjson = {
    	log.info("INGRESANDO AL METODO listJson DEL CONTROLADOR VendedorController")
    	def vendedores = Vendedor.list(sort:"nombre",order:"asc")
    	render(contentType:'text/json'){
    		rows{
    			vendedores.each{
    				row(id:it.id,nombre:it.nombre)
    			}
    		}
    	}
    }

    def show = {
        def vendedorInstance = Vendedor.get( params.id )

        if(!vendedorInstance) {
            flash.message = "Vendedor not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ vendedorInstance : vendedorInstance ] }
    }

    def delete = {
        def vendedorInstance = Vendedor.get( params.id )
        if(vendedorInstance) {
            try {
                vendedorInstance.delete(flush:true)
                flash.message = "Vendedor ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Vendedor ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "Vendedor not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def vendedorInstance = Vendedor.get( params.id )

        if(!vendedorInstance) {
            flash.message = "Vendedor not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ vendedorInstance : vendedorInstance ]
        }
    }

    def update = {
        def vendedorInstance = Vendedor.get( params.id )
        if(vendedorInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(vendedorInstance.version > version) {
                    
                    vendedorInstance.errors.rejectValue("version", "vendedor.optimistic.locking.failure", "Another user has updated this Vendedor while you were editing.")
                    render(view:'edit',model:[vendedorInstance:vendedorInstance])
                    return
                }
            }
            vendedorInstance.properties = params
            if(!vendedorInstance.hasErrors() && vendedorInstance.save()) {
                flash.message = "Vendedor ${params.id} updated"
                redirect(action:show,id:vendedorInstance.id)
            }
            else {
                render(view:'edit',model:[vendedorInstance:vendedorInstance])
            }
        }
        else {
            flash.message = "Vendedor not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def create = {
    	log.info("INGRESANDO EL METODO create DE VendedorController")
        def vendedorInstance = new Vendedor()
        vendedorInstance.properties = params
        return ['vendedorInstance':vendedorInstance]
    }

    def save = {
    	
        def vendedorInstance = new Vendedor(params)
        if(!vendedorInstance.hasErrors() && vendedorInstance.save()) {
            flash.message = "Vendedor ${vendedorInstance.id} created"
            redirect(action:show,id:vendedorInstance.id)
        }
        else {
            render(view:'create',model:[vendedorInstance:vendedorInstance])
        }
    }
    
    
    def savejson = {
    	log.info("INGRESANDO AL METODO savejson DE VendedorController")
    	log.debug("Parametros JSON: $params")
    	def errorList
    	def vendedorInstance = new Vendedor(params)
    	if(!vendedorInstance.hasErrors() && vendedorInstance.save()){
    		log.info("LA INSTANCIA DE Vendedor SE GUARDO CORRECTAMENTE")
    		render(contentType:"text/json"){
    			success true
    			idVendedor vendedorInstance.id
    		}
    	}else{
    		log.info("ERROR DE VALIDACION EN INSTANCIA DE Vendedor")
    		vendedorInstance.errors.allErrors.each{error->
    			error.codes.each{
    				if(g.message(code)!=it)
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
    	log.info("INGRESANDO AL METODO updatejson DE VendedorController")
    	log.debug("Parametros: $params")
    	def vendedorInstance = Vendedor.get(params.id)
    	if(vendedorInstance){
    		/*if(params.version){
    			if(params.version){
    				def version = params.version.toLong()
    				if(vendedorInstance.version > version){
    					vendedorInstance.errors.rejectValue
    				}
    			}
    		}*/
    		
    		vendedorInstance.properties=params
    		if(!vendedorInstance.hasErrors() && vendedorInstance.save()){
    			log.info("Instancia de vendedor guardada, renderizando json")
    			render(contentType:"text/json"){
    				success true
    			}
    		}else{
    			log.info("Error de validacion en Vendedor")
    			vendedorInstance.errors.allErrors.each{error->
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
    		log.info("Vendedor no encontrado con id ${params.id}")
    		render(contentType:"text/json"){
    			success false
    			errors{
    				error(title:"Vendedor con id ${params.id} no encontrado")
    			}
    		}
    	}
    	
    }

    void deletejson(){
    	log.info("INGRESANDO AL METODO deletejson DEL CONTROLADOR VendedorController")
    	log.debug("PARAMETROS: $params")
    	def vendedorInstance = Vendedor.get(params.id)
    	if(vendedorInstance){
    		try{
    			vendedorInstance.delete(flush:true)
    			log.info("VENDEDOR ELIMINADO")
    			render(contentType:"text/json"){
    				success true
    			}
    		}catch(org.springframework.dao.DataIntegrityViolationException e){
    			log.info("ERROR DE INTEGRIDAD AL INTENTAR ELIMINAR EL VENDEDOR $vendedorInstance.Id")
    			render(contentType:"text/json"){
    				success false
    				msg "No se puede eliminar el vendedor porque es referenciado en otros datos"
    			}
    		}
    	}else{
    		log.info("EL VENDEDOR CON ID $params.id NO PUDO SER ENCONTRADO")
    		render(contentType:"text/json"){
    			success false
    			msg "EL VENDEDOR CON ID $params.id NO PUDO SER ENCONTRADO"
    		}
    	}
    }
}
