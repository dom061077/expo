package com.rural

import grails.converters.JSON

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
      	log.info("PARAMETROS: "+params)
        def empresaInstance = Empresa.get( params.id )
        def errorList
        if(empresaInstance) {
            try {
                empresaInstance.delete(flush:true)
                log.info("EMPRESA CON ID: "+params.id+" ELIMINADA")
                //flash.message = "Empresa ${params.id} deleted"
                //redirect(action:list)
                render(contentType:"text/json"){
                	respuesta(success:true,title:"El registro se eliminó correctamente")
                }
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
            	log.info("ERROR DE INTEGRIDAD AL TRATAR DE ELIMINAR LA EMPRESA CON ID: "+params.id)
                //flash.message = "Empresa ${params.id} could not be deleted"
                //redirect(action:show,id:params.id)
                render(contentType:"text/json"){
                	success false
                	title "No se puede eliminar la empresa porque esta referenciada por otros datos"
                }
            }
        }
        else {
            //flash.message = "Empresa not found with id ${params.id}"
            //redirect(action:list)
            log.info("Empresa con id ${params.id} no encontrada")
            render(contentType:"text/json"){
            	respuesta(success:false,
            		title:"Empresa con id ${params.id} no encontrada")
            }
        }
    }

    def editempresajson = {
    	log.info("INGRESANDO AL METODO editJson DE EMPRESACONTROLLER")
    	log.debug("Params: "+params)
    	def empresaInstance = Empresa.get(params.id)
    	log.debug("nombre de la localidad: "+empresaInstance.localidad.nombreLoc)
    	render(contentType:'text/json'){
    		success true
    		data(id:empresaInstance.id,nombre:empresaInstance.nombre
    			 ,nombreRepresentante:empresaInstance.nombreRepresentante
    			 ,telefono1:empresaInstance.telefono1
    			 ,telefono2:empresaInstance.telefono2
    			 ,telefonoRepresentante1:empresaInstance.telefonoRepresentante1
    			 ,telefonoRepresentante2:empresaInstance.telefonoRepresentante2
    			 ,telefonoRepresentante3:empresaInstance.telefonoRepresentante3
    			 ,cuit:empresaInstance.cuit
    			 ,direccion:empresaInstance.direccion
    			 ,provinciaLn:empresaInstance.localidad.departamento.provincia.nombre
    			 ,departamentoLn: empresaInstance.localidad.departamento.nombreDep
    			 ,localidadAux: empresaInstance.localidad.nombreLoc
    			 ,localidadId: empresaInstance.localidad.id
    			 ,vendedorId: empresaInstance.vendedor.id
    			 ,vendedor:empresaInstance.vendedor.nombre
    			 ,rubro:empresaInstance.subrubro.rubro.nombreRubro
    			 ,rubroId:empresaInstance.subrubro.rubro.id
    			 ,subrubro:empresaInstance.subrubro.nombreSubrubro
    			 ,subrubroId: empresaInstance.subrubro.id
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
    	log.debug("Parametros: ${params}")
    	log.debug("Params.id: "+params.id)
        def empresaInstance = Empresa.get( params.id )
        def expos = JSON.parse(params.exposempresajson)
        def empIterator = null
        def exposJsonIterator = null
        def isnew
        def isdeleted
        def expoJson=null
        def e = null
        def errorList = null
        //aqui determino las expos que se van a agregar
        expos.each{
    		empIterator = empresaInstance.expos.iterator()
    		e=null
    		isnew=true
    		while(empIterator.hasNext()){
    			e=empIterator.next()
    			if(e.id==it.id) isnew=false
    		}
    		if (isnew){
    			empresaInstance.addToExpos(Exposicion.get(it.id))
    			log.debug "SE AGREGO UNA EXPOSICION A LA EMPRESA"
    		}
    	}
    	//aqui determino las expos que se van a eliminar
    	
    	empIterator = empresaInstance.expos.iterator()
    	while(empIterator.hasNext()){
    		e=empIterator.next()
    		exposJsonIterator = expos.iterator()
    		expoJson=null
    		isdeleted=true
	    	while (exposJsonIterator.hasNext()){
	    		expoJson=exposJsonIterator.next()
	    		log.debug ("Id de expo JSON: "+expoJson.id+" Id de expo Empresa "+ e.id)
	    		if((expoJson.id.toString()).compareTo(e.id.toString())==0){
	    			isdeleted=false
	    			log.debug("Para NO borrar "+e)
	    		}
	    	}
    		if(isdeleted)
    			empIterator.remove()
    	}
    	
    	
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
                //flash.message = "Empresa ${params.id} updated"
                //redirect(action:show,id:empresaInstance.id)
                log.info("Intancia de empresa guardada, renderizando json")
                render(contentType:"text/json"){
                	success true
                }
            }
            else {
                //render(view:'edit',model:[empresaInstance:empresaInstance])
            	log.info("Error de validacion en departamento")
            	empresaInstance.errors.allErrors.each{error->
            		error.codes.each{
            			if(g.message(code:it)!=it)
            				errorList.add(g.message(code:it))
            		}
            	}

                render(contentType:"text/json"){
                	success false
                	errorList.each{
                		errors{error(title:it)}
                	}
                } 
                
            }
        }
        else {
            //flash.message = "Empresa not found with id ${params.id}"
            log.info("Empresa no encontrada con id ${params.id}")
            render(contentType:'text/json'){
            	success false
            	errors{error(title:"Empresa con id ${params.id} no encontrada")}
            }
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
    	log.info("INGRESANDO AL METODO savejson DE EmpresaController")
    	log.debug("Parametros Json: "+params)
        def empresaInstance = new Empresa(params)
    	log.debug("Valor de Instancia Empresa antes de salvar: "+empresaInstance)
		def expos = JSON.parse(params.exposempresajson)
		log.debug("Exposiciones cargadas: "+expos)
		Exposicion expo
    	expos.each{
			log.debug("Cada item de las expos en json es: "+it)
			expo = Exposicion.get(it.id)
			empresaInstance.addToExpos(expo)
		}

		
        if(!empresaInstance.hasErrors() && empresaInstance.save()) {
            render(contentType:"text/json") {
					success true
				}
        }
        else {
        	empresaInstance.errors.allErrors.each{
        		it.arguments.each{arg->
        			log.debug("Argumento: "+arg)
        		}
        		it.codes.each{cod->
        			log.debug("Codigos: "+cod)
        		}
        	}
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
    
    def listexpos = {
    	log.info("INGRESANDO AL METODO listexpos DE EmpresaController")
    	log.debug("PARAMETROS: "+params)
    	
    	
    	def empresa = Empresa.get(params.id)
    	
    	render(contentType:"text/json"){
    		total empresa.expos.count()
    		rows{
    			empresa.expos.each{
    				row(id:it.id,nombre:it.nombre,isnew:false)
    			}
    		}
    	}
    	
    }
    
    //****************************m�todos para el manejo inserci�n a partir de archivos excel********
    
    def upload = {
    	def file = request.getFile('myFile')
    }
    
    def uploadInfo = {
            def progressMap = session.getAttribute("progressMap")
            def progressStatus = session.getAttribute("progressStatus")
            
            if (!progressMap) {
                render("No ProgressMap you Dweeb(tm)!")
            }
            
            //if we don't have progress info in the session, it could
            //indicate that the file upload was to small to require streaming
            //and possibly finished before we could check progress
            if (progressMap?.bytesRead == null){
                render(builder:'json'){
        		    bytesRead(1 )   
        			totalSize(1 )
        			status(AjaxProgressListener.STATUS_DONE)
        		}   
            }
                
            
            //Aahh.. JSON builders how I love thee :)
    		render(builder:'json'){
    		    bytesRead(progressMap['bytesRead'] )   
    			totalSize(progressMap['length'] )
    			status(progressStatus)
    		}
    
    }
    
}
