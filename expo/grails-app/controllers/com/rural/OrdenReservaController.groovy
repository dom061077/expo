package com.rural

import com.rural.seguridad.*
import grails.converters.JSON
import java.util.StringTokenizer
import com.rural.OrdenReserva 

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

    def showtorecibo = {
    	log.info("INGRESANDO AL METODO showtorecbo DEL CONTROLADOR OrdenReservaController")
    	log.debug("PARAMETROS $params")
    	def ordenReserva = OrdenReserva.get(params.id)
    	def saldo = ordenReserva.total
    	ordenReserva.recibos.each{
    		saldo = saldo - it.total
    	}
    	render(contentType : "text/json"){
    		success true
    		data(nombreempresa:ordenReserva.empresa.nombre,numero 	:		ordenReserva.numero,
    		saldoorden 	:	saldo)
    	}
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
	    		def lote = Lote.get(it.lote_id)
	    		ord.addToDetalle(new DetalleServicioContratado(lote:lote,subTotal:it.subTotal))
	    		total=total+it.subTotal
	    	}
	    	ord.total=total
    	}
    	
    	def iterarConceptos = {OrdenReserva ord, def conceptos->
    		def tipoConcepto
    		def total=0
    		conceptos.each{
    			//tipoConcepto = TipoConcepto.get(new Long(it.id))
    			ord.addToOtrosconceptos(new OtrosConceptos(descripcion:it.descripcion,subTotal:it.subTotal))
    			total=total+it.subTotal
    		}
    		ord.total=total
    	}
    	
    	def ordenReservaInstance = new OrdenReserva(params)
    	log.debug("LOS DATOS DE EMPRESA ASIGNADOS POR LA ORDEN DE RESERVA SON: "+ordenReservaInstance.empresa.properties)
    	ordenReservaInstance.porcentajeResIns=(Iva.get(params.porcentajeResIns)).porcentaje
    	//ordenReservaInstance.porcentajeResNoIns=new Float(params.porcentajeResNoIns)
    	def detallejson = JSON.parse(params.detallejson)
    	def otrosconceptosjson = JSON.parse(params.otrosconceptosjson)
    	def productosjson = JSON.parse(params.productosjson)
		    	
    	productosjson.each{
    		ordenReservaInstance.addToProductos(new ProductoExpuesto(descripcion:it.descripcion))
    		
    	}
    	def empresaInstance
    	if (params.id){
	    	empresaInstance = Empresa.get(params.id)
	    	empresaInstance.properties=ordenReservaInstance.empresa.properties
	    	empresaInstance.usuario = authenticateService.userDomain()
		}else{
			empresaInstance = ordenReservaInstance.empresa
			empresaInstance.usuario=authenticateService.userDomain()
			
			log.debug("PROPIEDADES DE EMPRESA: $params.empresa.nombre, $params.empresa.cuit")
			log.debug("PROPIEDADES DE ORDEN DE RESERVA: "+ordenReservaInstance.properties)
		}    
					
   		ordenReservaInstance.usuario=authenticateService.userDomain()
   		iterarDetalleJson(ordenReservaInstance,detallejson)
   		iterarConceptos(ordenReservaInstance,otrosconceptosjson)
   		log.debug("PORCENTAJE IVA RESINS: "+ordenReservaInstance.porcentajeResIns)
   		log.debug("PORCENTAJE IVA RESNOINS: "+ordenReservaInstance.porcentajeResNoIns)
   		try{
			ordenReservaInstance=ordenReservaService.generarOrdenReserva(ordenReservaInstance,empresaInstance)
			render(contentType: "text/json"){
				success true
				ordenid ordenReservaInstance.id
			}
		}catch(OrdenReservaException e){
			render(contentType: "text/json"){			
				success false
				msg e.message
			}
		}   	
    }
    
    def ordenreservareporte = {
    	log.info("INGRESANDO AL METODO ordenreservareporte DEL CONTROLADOR OrdenReservaController")
    	log.debug("PARAMETROS ENVIADOS: "+params)
    	def ordenReservaInstance = OrdenReserva.get(params.id)
    	log.debug(ordenReservaInstance.empresa.nombre)
    	log.debug(ordenReservaInstance.empresa.vendedor.nombre)
    	ordenReservaInstance.detalle.each{
    		log.debug(it)
    		log.debug(it.lote.sector.nombre)
    	}
    	ordenReservaInstance.otrosconceptos.each{
    		log.debug(it.subTotal)
    	}
    	ordenReservaInstance.productos.each{
    		log.debug(it)
    	}
    	log.debug(ordenReservaInstance.expo.nombre)
    	log.debug(ordenReservaInstance.usuario.userRealName)
    	log.debug(ordenReservaInstance.empresa.localidad.nombreLoc)
    	log.debug(ordenReservaInstance.empresa.localidad.provincia.nombre)
    	List ordenList = new ArrayList()
    	ordenList.add(ordenReservaInstance)
    	ordenList.add(ordenReservaInstance)    	
    	ordenList.add(ordenReservaInstance)    	

		String pathtofile = servletContext.getRealPath("/reports/images")+"/"+ordenReservaInstance.expo.nombre.trim()+".jpg"
		if(ordenReservaInstance.expo.image){
			FileOutputStream foutput = new FileOutputStream(new File(pathtofile))
			foutput.write(ordenReservaInstance.expo.image)
			foutput.flush()
		}
    	
    	
    	log.debug("Orden Reserva: $ordenReservaInstance")
		String reportsDirPath = servletContext.getRealPath("/reports/");
		params.put("reportsDirPath", reportsDirPath);
		params.put("logo",ordenReservaInstance.expo.nombre.trim()+".jpg")
		log.debug("Parametros: $params")
		chain(controller:'jasper',action:'index',model:[data:ordenList],params:params)
    }
    
    def anularordenreserva = {
    	log.info("INGRESANDO AL METODO anularordenreserva DEL CONTROLADOR OrdenReservaController")
    	log.debug("PARAMETROS "+params)
    	try{
    		ordenReservaService.anularOrdenReserva(new Long(params.id))
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
    
    def ivajson = {
    	log.info("INGRESANDO AL METODO ivajson DEL CONTROLADOR OrdenReservaController")
    	log.debug("PARAMETROS $params")
    	def iva=Iva.list()
    	render(contentType: "text/json"){
    		success true
    		rows{
    			iva.each{
    				row(id:it.id,descripcion:it.descripcion)
    			}
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
    	def totalOrdenes=0
    	def ordenes=null
    	if(params.fieldSearch=="numero"){
	    	totalOrdenes = OrdenReserva.createCriteria().count{
	    		and{
	    			eq('numero',new Long(params?.searchCriteria))
	    			//eq('numero',null)
	    			eq('anulada',false)
    			}
	    	}
	    	ordenes = OrdenReserva.createCriteria().list(){
	    		and{
		    		eq('numero',new Long(params?.searchCriteria))
		    		//eq('numero',null)
		    		eq('anulada',false)
	    		}
	    	}
    	}

    	if(params.fieldSearch=="empresa.nombre"){
	    	totalOrdenes = OrdenReserva.createCriteria().count{
				and{	    		
		    		empresa{
		    			like('nombre','%'+params?.searchCriteria+'%')
		   			}
		   			eq('anulada',false)
	   			}
	    	}
	    	ordenes = OrdenReserva.createCriteria().list(){
	    		and{
		    		empresa{
		   				like('nombre','%'+params?.searchCriteria+'%')
		   				if(params.sort=="nombre")
				   			order('nombre',params.dir.toLowerCase())
		    		}
		    		eq('anulada',false)
		    		if(params.sort=="total")
		    			order('total',params.dir.toLowerCase())
		    		if(params.sort=="fechaAlta")
		    			order('fechaAlta',params.dir.toLowerCase())
		    		if(params.sort=="numero")
		    			order('numero',params.dir.toLowerCase())
		    		if(params.sort=="sector" || params.sort=="lote"){
		    			detalle{
		    				lote{
		    					if(params.sort=="lote"){
		    						order('nombre',params.dir.toLowerCase())
		    					}
		    					if(params.sort="sector"){
		    						sector{
		    							order('nombre',params.dir.toLowerCase())
	    							}
		    					}
		    				}
		    			}
		    		}		
		    				
	    		}
	    	}
    	}
    	
    	
    	log.debug("Cantidad de Ordenes Consultadas: "+ordenes?.size()+" - total count() $totalOrdenes")
    	def totalCancelado=0
    	def saldo=0
    	def flagdetalle = false
    	totalOrdenes=0
    	render(contentType:"text/json"){
    		rows{
    			ordenes.each{
    				totalCancelado=0
    				saldo=0
    				it.recibos.each{ 
    					if(!it.anulado)
    						totalCancelado=totalCancelado+it.total
    				}
    				saldo=it.total-totalCancelado
    				def orden=it
    				flagdetalle=false	
    				it.detalle.each{
    					flagdetalle=true
    					row(id:orden.id,numero:orden.numero,fechaAlta:orden.fechaAlta,total:orden.total,anio:orden.anio,expoNombre:orden.expo.nombre
    						,sector:it.lote.sector.nombre
    						,lote:it.lote.nombre
    						,nombre:orden.empresa.nombre,totalCancelado:totalCancelado,saldo:saldo)
   					}
   					
   					if(!flagdetalle)
    					row(id:orden.id,numero:orden.numero,fechaAlta:orden.fechaAlta,total:orden.total,anio:orden.anio,expoNombre:orden.expo.nombre
    						,sector:""
    						,lote:""
    						,nombre:orden.empresa.nombre,totalCancelado:totalCancelado,saldo:saldo)
    			}
    		}
   			total totalOrdenes
    	}
    }

    
    
    def avancedlist = {
    	log.info("INGRESANDO AL METODO avancedlist DEL CONTROLLER OrdenReservaController")
    	log.debug("PARAMETROS: $params")
    	
    	def ordenProps = OrdenReserva.metaClass.properties*.name
    	def pagingconfig = [
    		max: params.limit as Integer?:10,
    		offset: params.start as Integer?:0
    	]
    	def ordenes = OrdenReserva.createCriteria().list(pagingconfig){
    		and{
    			if(params.filtroempresa){
    				empresa{
		    			params.each{field,value->
		    				if(ordenProps.grep('empresa.'+field) && value){
		    					like(field,value)
		    				}	
		    			}
	    			}
    			}
    			eq('anulada',false)
    		}
    	} 
		render(contentType:"text/json"){
			success true
			rows{
				ordenes.each{
					row(id:it.id,fechaalta:it.fechaAlta,nombreempresa:it.empresa.nombre)
				}
			}
		}
		
		/*StringTokenizer token = StringTokenizer(params.propiedad,'.')
		
    	def ordenes = com.rural.OrdenReserva.withCriteria{
			
	    	while(token.hasMoreElements()){
	    			
	    	}
    	}*/
    }
		
    
}
