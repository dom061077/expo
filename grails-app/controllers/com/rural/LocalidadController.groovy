

package com.rural

class LocalidadController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

	def listjson = {
		def c = Localidad.createCriteria()
		def localidades = c.list{
			departamento{
				eq('nombre',params.departamentonombre)
			}
		}
    	render(contentType:'text/json'){
    		rows{
    			localidades.each{
    				row(id:it.id,nombre:it.nombre,sort_order:0)
    			}
    		}
    	}		
	}

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ localidadInstanceList: Localidad.list( params ), localidadInstanceTotal: Localidad.count() ]
    }

    def show = {
        def localidadInstance = Localidad.get( params.id )

        if(!localidadInstance) {
            flash.message = "Localidad not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ localidadInstance : localidadInstance ] }
    }

    def delete = {
        def localidadInstance = Localidad.get( params.id )
        if(localidadInstance) {
            try {
                localidadInstance.delete(flush:true)
                flash.message = "Localidad ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Localidad ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "Localidad not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def localidadInstance = Localidad.get( params.id )

        if(!localidadInstance) {
            flash.message = "Localidad not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ localidadInstance : localidadInstance ]
        }
    }

    def update = {
        def localidadInstance = Localidad.get( params.id )
        if(localidadInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(localidadInstance.version > version) {
                    
                    localidadInstance.errors.rejectValue("version", "localidad.optimistic.locking.failure", "Another user has updated this Localidad while you were editing.")
                    render(view:'edit',model:[localidadInstance:localidadInstance])
                    return
                }
            }
            localidadInstance.properties = params
            if(!localidadInstance.hasErrors() && localidadInstance.save()) {
                flash.message = "Localidad ${params.id} updated"
                redirect(action:show,id:localidadInstance.id)
            }
            else {
                render(view:'edit',model:[localidadInstance:localidadInstance])
            }
        }
        else {
            flash.message = "Localidad not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def create = {
        def localidadInstance = new Localidad()
        localidadInstance.properties = params
        return ['localidadInstance':localidadInstance]
    }

    def save = {
        def localidadInstance = new Localidad(params)
        if(!localidadInstance.hasErrors() && localidadInstance.save()) {
            flash.message = "Localidad ${localidadInstance.id} created"
            redirect(action:show,id:localidadInstance.id)
        }
        else {
            render(view:'create',model:[localidadInstance:localidadInstance])
        }
    }
}
