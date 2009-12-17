package com.rural

class EmpresaController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']
    
    def search = {
    	log.info("INGRESANDO AL METODO search de EmpresaController")
    	log.debug("Parametros: "+params)
    	render(contentType:'text/json'){
    		success true
    		if(params.searchCriteria)
    			searchCriteria params.searchCriteria
    	}
    }

    def listjson = {
    	log.info("INGRESANDO AL METODO listjson de EmpresaController")
    	log.debug("Parametros: "+params)
    	def empresas = Empresa.createCriteria().list(
    					max: params.limit,
    					offset: params.start,
    					sort: 'nombre',
    					order:'asc'
    			){like('nombre','%'+params.searchCriteria+'%')}
    	log.debug("Cantidad de Empresas consultadas: "+Empresa.count())
    	render(contentType:'text/json'){
    		total Empresa.count()
    		rows{
    			empresas.each{
    				row(id:it.id,nombre:it.nombre,nombreRepresentante:it.nombreRepresentante,telefono1:it.telefono1)
    			}
    			
    		}
    	}
    }
    
    def list = {
    	log.info("INGRESANDO AL METODO list DE EmpresaController")
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ empresaInstanceList: Empresa.list( params ), empresaInstanceTotal: Empresa.count() ]
    }

    def show = {
       	log.info("INGRESANDO AL METODO show DE EmpresaController")
        def empresaInstance = Empresa.get( params.id )

        if(!empresaInstance) {
            flash.message = "Empresa not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ empresaInstance : empresaInstance ] }
    }

    def delete = {
      	log.info("INGRESANDO AL METODO delete DE EmpresaController")
        def empresaInstance = Empresa.get( params.id )
        if(empresaInstance) {
            try {
                empresaInstance.delete(flush:true)
                flash.message = "Empresa ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Empresa ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "Empresa not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def editempresajson = {
    	log.info("INGRESANDO AL METODO editJson DE EMPRESACONTROLLER")
    	log.debug("Params: "+params)
    	def empresaInstance = Empresa.get(params.id)
    	render(contentType:'text/json'){
    		success true
    		data(id:empresaInstance.id,nombre:empresaInstance.nombre
    			 ,nombreRepresentante:empresaInstance.nombreRepresentante
    			 ,telefono1:empresaInstance.telefono1
    			 ,telefono2:empresaInstance.telefono2
    			 ,cuit:empresaInstance.cuit
    			 ,direccion:empresaInstance.direccion
    			 ,provinciaLn:e
    			)
    	}
    	
    }
    
    def edit = {
    	log.info("INGRESANDO AL METODO edit DE EmpresaController")    
        def empresaInstance = Empresa.get( params.id )

        if(!empresaInstance) {
            flash.message = "Empresa not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ id: params.id ]
        }
    }

    def update = {
    	log.info("INGRESANDO AL METODO update DE EmpresaController")
        def empresaInstance = Empresa.get( params.id )
        if(empresaInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(empresaInstance.version > version) {
                    
                    empresaInstance.errors.rejectValue("version", "empresa.optimistic.locking.failure", "Another user has updated this Empresa while you were editing.")
                    render(view:'edit',model:[empresaInstance:empresaInstance])
                    return
                }
            }
            empresaInstance.properties = params
            if(!empresaInstance.hasErrors() && empresaInstance.save()) {
                flash.message = "Empresa ${params.id} updated"
                redirect(action:show,id:empresaInstance.id)
            }
            else {
                render(view:'edit',model:[empresaInstance:empresaInstance])
            }
        }
        else {
            flash.message = "Empresa not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def create = {
    	log.info "INGRESANDO AL METODO CREATE DE EmpresaController"
        def empresaInstance = new Empresa()
        empresaInstance.properties = params
        return ['empresaInstance':empresaInstance]
    }

    def save = {
    	log.info ("INGRESANDO AL METODO SAVE DE EmpresaController")

        def empresaInstance = new Empresa(params)
        
        if(!empresaInstance.hasErrors() && empresaInstance.save()) {
            flash.message = "Empresa ${empresaInstance.id} created"
            redirect(action:show,id:empresaInstance.id)
        }
        else {
            render(view:'create',model:[empresaInstance:empresaInstance])
        }
    }
    
    def savejson = {
    	log.info ("INGRESANDO AL METODO savejson DE EmpresaController")
    	log.debug("Parametros Json: "+params)
        def empresaInstance = new Empresa(params)
    	log.debug("Valor de Instancia Empresa antes de salvar: "+empresaInstance)
        if(!empresaInstance.hasErrors() && empresaInstance.save()) {
            render(contentType:"text/json") {
					success true
				}
        }
        else {
            render(contentType:"text/json") {
					success false
					errors {
						empresaInstance.errors.allErrors.each {
							 title it.defaultMessage	
							 }
					}
				}
            
        }
    
    }
}
