

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
    	def subrubros = SubRubro.list()
    	render(contentType:"text/json"){
			total subrubros.size()    		
    		rows{

    			subrubros.each{
    				row(id:it.id,nombreSubrubro:it.nombreSubrubro)
    			}
    		}
    	}
    }
}
