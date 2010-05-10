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
        /*def ordenReservaInstance = new OrdenReserva()
        ordenReservaInstance.properties = params
        return ['ordenReservaInstance':ordenReservaInstance]*/
        log.info("INGRESANDO AL METODO create DEL CONTROLADOR OrdenReservaController")
        
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
    	log.debug("PARAMETROS DE INGRESO: "+params)
    	def iterarDetalleJson = {OrdenReserva ord, def detalle->
    		def total=0
	    	detalle.each{
	    		ord.addToDetalle(new DetalleServicioContratado(sector:it.sector,lote:it.lote,subTotal:it.subTotal))
	    		total=total+it.subTotal
	    	}
	    	ord.total=total
    	}
    	
    	def iterarConceptos = {OrdenReserva ord, def conceptos->
    		def tipoConcepto
    		def total=0
    		conceptos.each{
    			tipoConcepto = TipoConcepto.get(new Long(it.id))
    			ord.addToOtrosconceptos(new OtrosConceptos(descripcion:it.descripcion,subTotal:it.subTotal,tipo:tipoConcepto))
    			total=total+it.subTotal
    		}
    		ord.total=total
    	}
    	
    	def ordenReservaInstance = new OrdenReserva(params)
    	def detallejson = JSON.parse(params.detallejson)
    	def otrosconceptosjson = JSON.parse(params.otrosconceptosjson)
    	def productosjson = JSON.parse(params.productosjson)
    	productosjson.each{
    		ordenReservaInstance.addToProductos(new ProductoExpuesto(descripcion:it.descripcion))
    		
    	}
    	def empresaInstance = Empresa.get(params.id)
    	empresaInstance.properties=ordenReservaInstance.empresa
   		ordenReservaInstance.usuario=authenticateService.userDomain()
   	
   		iterarDetalleJson(ordenReservaInstance,detallejson)
   		iterarConceptos(ordenReservaInstance,otrosconceptosjson)
   		
		ordenReservaInstance=ordenReservaService.generarOrdenReserva(ordenReservaInstance,empresaInstance)
		render(contentType: "text/json"){
			success true
			ordenid ordenReservaInstance.id
		}   	
    }
    
    def ordenreservareporte = {
    	log.info("INGRESANDO AL METODO ordenreservareporte DEL CONTROLADOR OrdenReservaController")
    	log.debug("PARAMETROS ENVIADOS: "+params)
    	def ordenReservaInstance = OrdenReserva.get(params.id)
    	List ordenList = new ArrayList()
    	ordenList.add(ordenReservaInstance)
    	log.debug(ordenReservaInstance.empresa.nombre)
    	ordenReservaInstance.detalle.each{
    		log.debug(it)
    	}
    	ordenReservaInstance.otrosconceptos.each{
    		log.debug(it)
    	}
    	ordenReservaInstance.productos.each{
    		log.debug(it)
    	}
    	log.debug("Orden Reserva: $ordenReservaInstance")
		String reportsDirPath = servletContext.getRealPath("/reports/");
		params.put("reportsDirPath", reportsDirPath);
		log.debug("Parametros: $params")
		chain(controller:'jasper',action:'index',model:[data:ordenList],params:params)
    }
    
    def anularordenreserva = {
    	log.info("INGRESANDO AL METODO anularordenreserva DEL CONTROLADOR OrdenReservaController")
    	log.debug("PARAMETROS "+params)
    	try{
    		ordenReservaService.anularOrdenReserva(params.id)
    		render(contentType: "text/json"){
    			success true
    			
    		}
    	}catch(OrdenReservaException e){
    		render(contentType: "text/json"){
    			success false
    			msg e.message
    		}
    	}
    }
    
    def listjson = {
    	log.info("INGRESANDO AL METODO listjson DEL CONTROLADOR OrdenReservaController")
    	log.debug("PARAMETROS $params")
    	def pagingconfig = [
    		max: params.limit as Integer ?:10,
    		offset: params.start as Integer ?:0
    	]
    	
    	def totalOrdenes = OrdenReserva.createCriteria().count{
    		empresa{
    			like('nombre','%'+params?.searchCriteria+'%')
   			}
    	}
    	def ordenes = OrdenReserva.createCriteria().list(pagingconfig){
    		empresa{
   				like('nombre','%'+params?.searchCriteria+'%')
    		}
    	}
    	log.debug("Cantidad de Ordenes Consultadas: "+ordenes.size()+" - total count() $totalOrdenes")
    	render(contentType:"text/json"){
    		total totalOrdenes
    		rows{
    			ordenes.each{
    				row(id:it.id,numero:it.numero,fechaAlta:it.fechaAlta,total:it.total,anio:it.anio,expoNombre:it.expo.nombre,empresaNombre:it.empresa.nombre)
    			}
    		}
    	}
    }
}
