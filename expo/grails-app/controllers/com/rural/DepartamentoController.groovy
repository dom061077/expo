

package com.rural

class DepartamentoController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']
	
	def listjson = {
		log.info ("INGRESANDO AL METODO listjson DE DepartamentoController")
		def c = Departamento.createCriteria()
		def departamentos = c.list{
		    provincia{
		    	eq('nombre',params.provincianombre)
		    }
}
		
    	render(contentType:'text/json'){
    		rows{
    			departamentos.each{
    				row(id:it.id,nombreDep:it.nombreDep,sort_order:0)
    			}
    		}
    	}			
	}

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ departamentoInstanceList: Departamento.list( params ), departamentoInstanceTotal: Departamento.count() ]
    }

    def show = {
        def departamentoInstance = Departamento.get( params.id )

        if(!departamentoInstance) {
            flash.message = "Departamento not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ departamentoInstance : departamentoInstance ] }
    }

    def delete = {
        def departamentoInstance = Departamento.get( params.id )
        if(departamentoInstance) {
            try {
                departamentoInstance.delete(flush:true)
                flash.message = "Departamento ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Departamento ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "Departamento not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def departamentoInstance = Departamento.get( params.id )

        if(!departamentoInstance) {
            flash.message = "Departamento not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ departamentoInstance : departamentoInstance ]
        }
    }

    def update = {
        def departamentoInstance = Departamento.get( params.id )
        if(departamentoInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(departamentoInstance.version > version) {
                    
                    departamentoInstance.errors.rejectValue("version", "departamento.optimistic.locking.failure", "Another user has updated this Departamento while you were editing.")
                    render(view:'edit',model:[departamentoInstance:departamentoInstance])
                    return
                }
            }
            departamentoInstance.properties = params
            if(!departamentoInstance.hasErrors() && departamentoInstance.save()) {
                flash.message = "Departamento ${params.id} updated"
                redirect(action:show,id:departamentoInstance.id)
            }
            else {
                render(view:'edit',model:[departamentoInstance:departamentoInstance])
            }
        }
        else {
            flash.message = "Departamento not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def create = {
        def departamentoInstance = new Departamento()
        departamentoInstance.properties = params
        return ['departamentoInstance':departamentoInstance]
    }
    
    def savejson ={
    	log.debug("INGRESANDO AL METODO savejson DE DepartamentoController")
        def departamentoInstance = new Departamento(params)
        if(!departamentoInstance.hasErrors() && departamentoInstance.save()) {
            render(contentType:"text/json"){
            	success true
				nombreDep departamentoInstance.nombreDep
            }
        }
        else {
        	log.debug("Errores de validacion: "+departamentoInstance.errors)
            render(contentType:"text/json"){
            	success false
            } 

            
        }
    }

    def save = {
        def departamentoInstance = new Departamento(params)
        if(!departamentoInstance.hasErrors() && departamentoInstance.save()) {
			flash.message = "Departamento ${departamentoInstance.id} fue creado"
			redirect(action:show,id:departamentoInstance.id)
        }
        else {
        	departamentoInstance.errors.allErrors.each{error->
        		def code = {def codigo="hola"	
        					error.codes.each{
				        					if(g.message(code:it)!=it){
												codigo=g.message(code:it)											        						
				        					}
		        					} 
		        			 return codigo		
        					}
				log.debug("Mensaje final: "+code)        				
        	}
        
            render(view:'create',model:[departamentoInstance:departamentoInstance])
            
        }
    }
}
