

package com.rural

class EmpresaController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ empresaInstanceList: Empresa.list( params ), empresaInstanceTotal: Empresa.count() ]
    }

    def show = {
        def empresaInstance = Empresa.get( params.id )

        if(!empresaInstance) {
            flash.message = "Empresa not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ empresaInstance : empresaInstance ] }
    }

    def delete = {
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

    def edit = {
        def empresaInstance = Empresa.get( params.id )

        if(!empresaInstance) {
            flash.message = "Empresa not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ empresaInstance : empresaInstance ]
        }
    }

    def update = {
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
        def empresaInstance = new Empresa()
        empresaInstance.properties = params
        return ['empresaInstance':empresaInstance]
    }

    def save = {
        def empresaInstance = new Empresa(params)
        
        if(!empresaInstance.hasErrors() && empresaInstance.save()) {
            //flash.message = "Empresa ${empresaInstance.id} created"
            //redirect(action:show,id:empresaInstance.id)
            render(contentType:"text/json") {
					success true
					
				}
        }
        else {
            //render(view:'create',model:[empresaInstance:empresaInstance])
            render(contentType:"text/json") {
					success false
					errors {
						empresaInstance.errors.allErrors.each {
							 title it.defaultMessage	
							 }
						/*for(alb in a.albums) {
							album name:alb.title
						}*/
					}
				}
            
        }
    }
}
