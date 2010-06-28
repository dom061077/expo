

package com.rural

class SubRubroController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ subRubroInstanceList: SubRubro.list( params ), subRubroInstanceTotal: SubRubro.count() ]
    }

    def show = {
        def subRubroInstance = SubRubro.get( params.id )

        if(!subRubroInstance) {
            flash.message = "SubRubro not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ subRubroInstance : subRubroInstance ] }
    }

    def delete = {
        def subRubroInstance = SubRubro.get( params.id )
        if(subRubroInstance) {
            try {
                subRubroInstance.delete(flush:true)
                flash.message = "SubRubro ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "SubRubro ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "SubRubro not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        /*def subRubroInstance = SubRubro.get( params.id )

        if(!subRubroInstance) {
            flash.message = "SubRubro not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ subRubroInstance : subRubroInstance ]
        }*/
        log.info("INGRESANDO AL METODO edit DEL CONTROLLER SubRubroController")
        log.info("PARAMETROS $params")
        return[id:params.id]
    }

    def update = {
        def subRubroInstance = SubRubro.get( params.id )
        if(subRubroInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(subRubroInstance.version > version) {
                    
                    subRubroInstance.errors.rejectValue("version", "subRubro.optimistic.locking.failure", "Another user has updated this SubRubro while you were editing.")
                    render(view:'edit',model:[subRubroInstance:subRubroInstance])
                    return
                }
            }
            subRubroInstance.properties = params
            if(!subRubroInstance.hasErrors() && subRubroInstance.save()) {
                flash.message = "SubRubro ${params.id} updated"
                redirect(action:show,id:subRubroInstance.id)
            }
            else {
                render(view:'edit',model:[subRubroInstance:subRubroInstance])
            }
        }
        else {
            flash.message = "SubRubro not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def create = {
        def subRubroInstance = new SubRubro()
        subRubroInstance.properties = params
        return ['subRubroInstance':subRubroInstance]
    }

    def save = {
        def subRubroInstance = new SubRubro(params)
        if(!subRubroInstance.hasErrors() && subRubroInstance.save()) {
            flash.message = "SubRubro ${subRubroInstance.id} created"
            redirect(action:show,id:subRubroInstance.id)
        }
        else {
            render(view:'create',model:[subRubroInstance:subRubroInstance])
        }
    }
    //-----------------------------metodos json---------------------
    
    def savejson = {
    	log.info("INGRESANDO AL METODO savejson DEL CONTROLLER SubRubroController")
    	log.debug("Parámetros: $params")
    	def errorList = []
    	def subrubroInstance = new SubRubro(params)
    	if (!subrubroInstance.hasErrors() && subrubroInstance.save()){
    		log.info("INSTANCIA DE SubRubro GUARDADA RENDERIZANDO JSON")
    		render(contentType:"text/json"){
    			success true
    			idSubRubro subrubroInstance.id
    		}
    	}else{
    		log.info("ERROR DE VALIDACION EN INSTANCIA DE SubRubro")
    		subrubroInstance.errors.allErrors.each{error->
    			error.codes.each{
    				if(g.message(code:it)!=it)
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
    	log.info("INGRESANDO AL METODO updatejson DEL CONTROLLER SubRubroController")
    	log.debug("Parámetros: $params")
    	def errorList = []
    	def subrubroInstance = SubRubro.get(params.id)
    	if(subrubroInstance){
    		/*if(params.version){
    			if(params.version){
    				def version = params.version.toLong()
    				if(subrubroInstance.version > version){
    					subrubroInstance.errors.rejectValue
    				}
    			}
    		}*/
    		
    		subrubroInstance.properties=params
    		if(!subrubroInstance.hasErrors() && subrubroInstance.save()){
    			log.info("Instancia de SubRubro guardada, renderizando json")
    			render(contentType:"text/json"){
    				success true
    			}
    		}else{
    			log.info("Error de validacion en SubRubro")
    			subrubroInstance.errors.allErrors.each{error->
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
    		log.info("subrubro no encontrado con id ${params.id}")
    		render(contentType:"text/json"){
    			success false
    			errors{
    				error(title:"subrubro con id ${params.id} no encontrado")
    			}
    		}
    	}
    }
    
    def showjson = {
    	log.info("INGRESANDO AL METODO showjson DEL CONTROLLER SubRubroController")
    	log.debug("Parámetros: $params")
    	def subrubroInstance = SubRubro.get(params.id)
    	if (subrubroInstance){
    		log.debug("Instancia de subrubro encontrada, renderizando json")
    		render(contentType:"text/json"){
    			success true	
    			data(id :subrubroInstance.id,nombreSubrubro: subrubroInstance.nombreSubrubro,'rubro.id':subrubroInstance.rubro.id)
    		}
    	}else{
    		log.debug("Instancia de subrubro no encontrada, renderizando json")
    		render(contentType:"text/json"){
    			success false
    		}
    	}
    	
    }
    
    def deletejson={
    	log.info("INGRESANDO AL METODO deletejson DEL CONTROLADOR SubRubroController")
    	log.debug("PARAMETROS: $params")
    	def subrubroInstance = SubRubro.get(params.id)
    	if(subrubroInstance){
    		try{
    			subrubroInstance.delete(flush:true)
    			log.info("SubRubro ELIMINADO")
    			render(contentType:"text/json"){
    				success true
    			}
    		}catch(org.springframework.dao.DataIntegrityViolationException e){
    			log.info("ERROR DE INTEGRIDAD AL INTENTAR ELIMINAR EL SubRubro $subrubroInstance.id")
    			render(contentType:"text/json"){
    				success false
    				msg "No se puede eliminar el SubRubro porque es referenciado en otros datos"
    			}
    		}
    	}else{
    		log.info("EL SubRubro CON ID $params.id NO PUDO SER ENCONTRADO")
    		render(contentType:"text/json"){
    			success false
    			msg "EL SubRubro CON ID $params.id NO PUDO SER ENCONTRADO"
    		}
    	}
    }

    def listjson={
    	log.info("INGRESANDO AL METODO listjson DEL CONTROLLER SubRubroController")
    	log.debug("PARAMETROS: $params")
    	def c = SubRubro.createCriteria()
    	def rubros = c.list{
    		like('nombreSubrubro','%'+params.searchCriteria+'%')
    	}
    	
    	def totalsubrubros = SubRubro.createCriteria().count{
    		like('nombreSubrubro','%'+params.searchCriteria+'%')
    	}
    	
    	render(contentType:'text/json'){
    		total totalsubrubros
    		rows{
    			rubros.each{
    				row(id:it.id,nombreSubrubro:it.nombreSubrubro)
    			}
    		}
    	}    	
    }
    
}
