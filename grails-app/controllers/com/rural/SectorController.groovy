

package com.rural

class SectorController {
    
    def index = { redirect(action:list,params:params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']
    
    def listjson = {
    	log.info("INGRESANDO AL METODO listjson DEL CONTROLLER SectorController")
    	log.debug("PARAMETROS INGRESADOS: $params")
    	def c = Sector.createCriteria()
    	def sectores = c.list{
    		expo{
    			eq('id',new Long(params.exposicion_id))
    		}
    	}
    	render(contentType:"text/json"){
    		total sectores.size()
    		rows{
    			sectores.each{
    				row(id:it.id,nombre:it.nombre)
    			}
    		}
    	}
    }

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ sectorInstanceList: Sector.list( params ), sectorInstanceTotal: Sector.count() ]
    }

    def show = {
        def sectorInstance = Sector.get( params.id )

        if(!sectorInstance) {
            flash.message = "Sector not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ sectorInstance : sectorInstance ] }
    }

    def delete = {
        def sectorInstance = Sector.get( params.id )
        if(sectorInstance) {
            try {
                sectorInstance.delete(flush:true)
                flash.message = "Sector ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "Sector ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "Sector not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def sectorInstance = Sector.get( params.id )

        if(!sectorInstance) {
            flash.message = "Sector not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ sectorInstance : sectorInstance ]
        }
    }

    def update = {
        def sectorInstance = Sector.get( params.id )
        if(sectorInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(sectorInstance.version > version) {
                    
                    sectorInstance.errors.rejectValue("version", "sector.optimistic.locking.failure", "Another user has updated this Sector while you were editing.")
                    render(view:'edit',model:[sectorInstance:sectorInstance])
                    return
                }
            }
            sectorInstance.properties = params
            if(!sectorInstance.hasErrors() && sectorInstance.save()) {
                flash.message = "Sector ${params.id} updated"
                redirect(action:show,id:sectorInstance.id)
            }
            else {
                render(view:'edit',model:[sectorInstance:sectorInstance])
            }
        }
        else {
            flash.message = "Sector not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def create = {
        def sectorInstance = new Sector()
        sectorInstance.properties = params
        return ['sectorInstance':sectorInstance]
    }

    def save = {
        def sectorInstance = new Sector(params)
        if(!sectorInstance.hasErrors() && sectorInstance.save()) {
            flash.message = "Sector ${sectorInstance.id} created"
            redirect(action:show,id:sectorInstance.id)
        }
        else {
            render(view:'create',model:[sectorInstance:sectorInstance])
        }
    }
}