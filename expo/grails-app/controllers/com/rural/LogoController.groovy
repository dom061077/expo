

package com.rural

class LogoController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
    	log.info "INGRESANDO AL CLOSURE list DEL CONTROLLER LogoController"
    	log.debug "PARAMETROS: $params"
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ logoInstanceList: Logo.list( params ), logoInstanceTotal: Logo.count(),exposicionId:params.expoid,exposicionNombre:params.exponombre ]
    }

    def show = {
        def logoInstance = Logo.get( params.id )

        if(!logoInstance) {
            flash.message = "Logo not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ logoInstance : logoInstance ] }
    }

    def delete = {
        def logoInstance = Logo.get( params.id )
        if(logoInstance) {
            try {
                logoInstance.delete(flush:true)
                flash.message = "Logo ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Logo ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "Logo not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def logoInstance = Logo.get( params.id )

        if(!logoInstance) {
            flash.message = "Logo not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ logoInstance : logoInstance ]
        }
    }

    def update = {
        def logoInstance = Logo.get( params.id )
        if(logoInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(logoInstance.version > version) {
                    
                    logoInstance.errors.rejectValue("version", "logo.optimistic.locking.failure", "Another user has updated this Logo while you were editing.")
                    render(view:'edit',model:[logoInstance:logoInstance])
                    return
                }
            }
            logoInstance.properties = params
            if(!logoInstance.hasErrors() && logoInstance.save()) {
                flash.message = "Logo ${params.id} updated"
                redirect(action:show,id:logoInstance.id)
            }
            else {
                render(view:'edit',model:[logoInstance:logoInstance])
            }
        }
        else {
            flash.message = "Logo not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def create = {
    	log.info "INGRESANDO AL CLOSURE create DEL CONTROLLER LogoController"
    	log.debug "PARAMETROS $params"
        def logoInstance = new Logo()
        logoInstance.properties = params
        return ['logoInstance':logoInstance,exposicionId:params.exposicionId,exposicionNombre:params.exposicionNombre]
    }

    def save = {
        def logoInstance = new Logo(params)
        if(!logoInstance.hasErrors() && logoInstance.save()) {
            flash.message = "Logo ${logoInstance.id} created"
            redirect(action:show,id:logoInstance.id)
        }
        else {
            render(view:'create',model:[logoInstance:logoInstance])
        }
    }
    
    def savejson = {
    	log.info "INGRESANDO AL CLOSURE savejson DEL CONTROLLER LogoController"
    	log.debug "PARAMETROS $params"
		def image = request.getFile('image')
		if (image?.empty || image?.size>1024*30){
			log.error "LA IMAGEN  SUPERA EL LIMITE PERMITIDO O ES UNA IMAGEN VACIA"
			render """{success:false,msg:'El tamaño máximo de la imagen es de 30 KB'}"""
			return
		}
		def logoInstance = new Logo(params)
		
    	if(!logoInstance.hasErrors() && logoInstance.save()){
			log.info "LA INSTACIA DE LA CLASE Logo SE GUARDO CORRECTAMENTE"
			render """{success:true},msg:''"""
    	}else{
			log.error "ERROR AL GUARDAR LA INSTACIA DE LA CLASE Logo "+logoInstance.errors.allErrors
			render """{success:false,msg:'Se produjo un error al tratar de guardar los datos'}"""
		}
    }
	
	
}
