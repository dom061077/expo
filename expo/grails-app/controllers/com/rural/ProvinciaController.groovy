

package com.rural

class ProvinciaController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']
    
    def listjson = {
    	def provincias = Provincia.list()
    	render(contentType:'text/json'){
    		rows{
    			provincias.each{
    				row(id:it.id,nombre:it.nombre,sort_order:0)
    			}
    		}
    	}
    }

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ provinciaInstanceList: Provincia.list( params ), provinciaInstanceTotal: Provincia.count() ]
    }

    def show = {
        def provinciaInstance = Provincia.get( params.id )

        if(!provinciaInstance) {
            flash.message = "Provincia not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ provinciaInstance : provinciaInstance ] }
    }

    def delete = {
        def provinciaInstance = Provincia.get( params.id )
        if(provinciaInstance) {
            try {
                provinciaInstance.delete(flush:true)
                flash.message = "Provincia ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Provincia ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "Provincia not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def provinciaInstance = Provincia.get( params.id )

        if(!provinciaInstance) {
            flash.message = "Provincia not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ provinciaInstance : provinciaInstance ]
        }
    }

    def update = {
        def provinciaInstance = Provincia.get( params.id )
        if(provinciaInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(provinciaInstance.version > version) {
                    
                    provinciaInstance.errors.rejectValue("version", "provincia.optimistic.locking.failure", "Another user has updated this Provincia while you were editing.")
                    render(view:'edit',model:[provinciaInstance:provinciaInstance])
                    return
                }
            }
            provinciaInstance.properties = params
            if(!provinciaInstance.hasErrors() && provinciaInstance.save()) {
                flash.message = "Provincia ${params.id} updated"
                redirect(action:show,id:provinciaInstance.id)
            }
            else {
                render(view:'edit',model:[provinciaInstance:provinciaInstance])
            }
        }
        else {
            flash.message = "Provincia not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def create = {
        def provinciaInstance = new Provincia()
        provinciaInstance.properties = params
        return ['provinciaInstance':provinciaInstance]
    }

    def save = {
        def provinciaInstance = new Provincia(params)
        if(!provinciaInstance.hasErrors() && provinciaInstance.save()) {
            flash.message = "Provincia ${provinciaInstance.id} created"
            redirect(action:show,id:provinciaInstance.id)
        }
        else {
            render(view:'create',model:[provinciaInstance:provinciaInstance])
        }
    }
}
