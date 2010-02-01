

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
}
