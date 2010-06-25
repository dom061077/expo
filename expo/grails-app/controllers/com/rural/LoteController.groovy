

package com.rural

class LoteController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

	def listjson = {
		log.info("INGRESANDO AL METODO listjson DEL CONTROLLER LoteController")
		log.debug("PARAMETROS $params")
		def c = Lote.createCriteria()
		def lotes = c.list{
			sector{
				eq('id', new Long(params.sector_id))
			}
		} 
		render(contentType:"text/json"){
			total lotes.size()
			rows{
				lotes.each{
					row(id:it.id,nombre:it.nombre)
				}
			}
		}
	}

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ loteInstanceList: Lote.list( params ), loteInstanceTotal: Lote.count() ]
    }

    def show = {
        def loteInstance = Lote.get( params.id )

        if(!loteInstance) {
            flash.message = "Lote not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ loteInstance : loteInstance ] }
    }

    def delete = {
        def loteInstance = Lote.get( params.id )
        if(loteInstance) {
            try {
                loteInstance.delete(flush:true)
                flash.message = "Lote ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Lote ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "Lote not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def loteInstance = Lote.get( params.id )

        if(!loteInstance) {
            flash.message = "Lote not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ loteInstance : loteInstance ]
        }
    }

    def update = {
        def loteInstance = Lote.get( params.id )
        if(loteInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(loteInstance.version > version) {
                    
                    loteInstance.errors.rejectValue("version", "lote.optimistic.locking.failure", "Another user has updated this Lote while you were editing.")
                    render(view:'edit',model:[loteInstance:loteInstance])
                    return
                }
            }
            loteInstance.properties = params
            if(!loteInstance.hasErrors() && loteInstance.save()) {
                flash.message = "Lote ${params.id} updated"
                redirect(action:show,id:loteInstance.id)
            }
            else {
                render(view:'edit',model:[loteInstance:loteInstance])
            }
        }
        else {
            flash.message = "Lote not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def create = {
        def loteInstance = new Lote()
        loteInstance.properties = params
        return ['loteInstance':loteInstance]
    }

    def save = {
        def loteInstance = new Lote(params)
        if(!loteInstance.hasErrors() && loteInstance.save()) {
            flash.message = "Lote ${loteInstance.id} created"
            redirect(action:show,id:loteInstance.id)
        }
        else {
            render(view:'create',model:[loteInstance:loteInstance])
        }
    }
    
    def savejson = {
    	log.info("INGRESANDO AL METODO savejson DEL CONTROLLER LoteController")
    	log.debug("PARAMETROS: $params")
    	
    }
}
