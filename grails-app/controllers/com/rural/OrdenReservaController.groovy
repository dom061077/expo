package com.rural

import com.rural.seguridad.*
import grails.converters.JSON

class OrdenReservaController {
    def ordenReservaService
    def authenticateService
    
    def index = { redirect(action:list,params:params) }
    	

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
        [ ordenReservaInstanceList: OrdenReserva.list( params ), ordenReservaInstanceTotal: OrdenReserva.count() ]
    }

    def show = {
        def ordenReservaInstance = OrdenReserva.get( params.id )

        if(!ordenReservaInstance) {
            flash.message = "OrdenReserva not found with id ${params.id}"
            redirect(action:list)
        }
        else { return [ ordenReservaInstance : ordenReservaInstance ] }
    }

    def delete = {
        def ordenReservaInstance = OrdenReserva.get( params.id )
        if(ordenReservaInstance) {
            try {
                ordenReservaInstance.delete(flush:true)
                flash.message = "OrdenReserva ${params.id} deleted"
                redirect(action:list)
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "OrdenReserva ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "OrdenReserva not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def edit = {
        def ordenReservaInstance = OrdenReserva.get( params.id )

        if(!ordenReservaInstance) {
            flash.message = "OrdenReserva not found with id ${params.id}"
            redirect(action:list)
        }
        else {
            return [ ordenReservaInstance : ordenReservaInstance ]
        }
    }

    def update = {
        def ordenReservaInstance = OrdenReserva.get( params.id )
        if(ordenReservaInstance) {
            if(params.version) {
                def version = params.version.toLong()
                if(ordenReservaInstance.version > version) {
                    
                    ordenReservaInstance.errors.rejectValue("version", "ordenReserva.optimistic.locking.failure", "Another user has updated this OrdenReserva while you were editing.")
                    render(view:'edit',model:[ordenReservaInstance:ordenReservaInstance])
                    return
                }
            }
            ordenReservaInstance.properties = params
            if(!ordenReservaInstance.hasErrors() && ordenReservaInstance.save()) {
                flash.message = "OrdenReserva ${params.id} updated"
                redirect(action:show,id:ordenReservaInstance.id)
            }
            else {
                render(view:'edit',model:[ordenReservaInstance:ordenReservaInstance])
            }
        }
        else {
            flash.message = "OrdenReserva not found with id ${params.id}"
            redirect(action:list)
        }
    }

    def create = {
        def ordenReservaInstance = new OrdenReserva()
        ordenReservaInstance.properties = params
        return ['ordenReservaInstance':ordenReservaInstance]
    }

    def save = {
        def ordenReservaInstance = new OrdenReserva(params)
        if(!ordenReservaInstance.hasErrors() && ordenReservaInstance.save()) {
            flash.message = "OrdenReserva ${ordenReservaInstance.id} created"
            redirect(action:show,id:ordenReservaInstance.id)
        }
        else {
            render(view:'create',model:[ordenReservaInstance:ordenReservaInstance])
        }
    }
    
    def generarordenreserva = {
    	log.info("INGRESANDO AL METODO generarordenreserva DEL CONTROLADOR OrdenReservaController")
    	def iterarDetalleJson = {OrdenReserva ord, def detalle->
	    	detalle.each{
	    		ord.addToDetalle(new DetalleServicioContratado(sector:it.sector,lote:it.lote,subtotal:it.subtotal))
	    	}
    	}
    
    	def ordenReservaInstance = new OrdenReserva(params)
    	def detallejson = JSON.parse(params.detallejson)
    	def otrosconceptosjson = JSON.parse(params.otrosconceptosjson)
    	def empresaInstance = Empresa.get(params.empresa.id)
    	empresaInstance.properties=ordenReservaInstance.empresa
   		ordenReservaInstance.usuario=authenticateService.userDomain()
   	
   		iterarDetalleJson(ordenReservaInstance,detallejson)
   	
    	/*otrosconceptosjson.each{ordenReservaInstance,item->
    		ordenReservaInstance.addToOtrosconceptos(new OtrosConceptos(descripcion:item.descripcion,subtotal:item.subtotal,tipo:item.tipoconcepto))
    	}*/
		ordenReservaService.generarOrdenReserva(ordenReservaInstance,empresaInstance)
		render(contentType: "text/json"){
			success true
			ordenid ordenReservaInstance.id
		}   	
    }
    
    
}
