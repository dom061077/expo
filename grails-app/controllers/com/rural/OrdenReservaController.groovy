package com.rural

import com.rural.seguridad.*
import grails.converters.JSON
import java.util.StringTokenizer
import java.util.List
import java.util.HashMap
import java.util.Collections
import com.rural.OrdenReserva
import com.rural.utils.EmpresaNombreComparator
import com.rural.utils.FechaOrdenComparator
import com.rural.utils.LoteComparator
import com.rural.utils.NumeroOrdenComparator
import com.rural.utils.SectorComparator

import jxl.*
import jxl.write.Label
import jxl.write.Number
import jxl.write.WritableWorkbook
import jxl.write.DateFormat
import jxl.write.DateTime
import jxl.write.WritableCellFormat
import jxl.write.WritableSheet
 
 

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
    		if(!it.anulado)
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
	    		def sector = Sector.get(it.sector_id)
	    		ord.addToDetalle(new DetalleServicioContratado(lote:lote,sector:sector,subTotal:it.subTotal))
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
   		println(Empresa.count())
   		/*if(empresaInstance.hasErrors() && !empresaInstance.save())
   			throw new Exception("empresaInstance errores: "+empresaInstance.errors.allErrors)
   		else
   			log.debug("SE SALVO BINE!!!!!")*/
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
    		log.debug(it.sector?.nombre)
    		log.debug(it.lote?.nombre)
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

    List  consultar(params){
    	log.debug("Dentro del closure consultar")
    	def pagingconfig = [
    	            		max: params.limit as Integer ?:10,
    	            		offset: params.start as Integer ?:0
    	            	]
    	            	def ordenes=null
    	            	def detalle=null
    	            	String hqlstr
    	            	List list = new ArrayList()
    	            	HashMap parameters = new HashMap()
    	            	def detalleservcontratado=null
    	            	if(params.fieldSearch=="numero"){
    	            		ordenes = OrdenReserva.findAll("from OrdenReserva o left outer join o.detalle det where det is null and o.numero= :numero and o.anulada=:anulada"
    	            					,[numero:new Long(params.searchCriteria),anulada:Boolean.parseBoolean(params.anulada)])
    	            		list.addAll(ordenes) 
    	            	}else{
    	            		log.debug("INGRESO POR EL ELSE DEL LA PREGUNTA DEL FIELDSEARCH")
    	        			hqlstr="from OrdenReserva o left outer join o.detalle det where det is null and o.anulada=:anulada"
    	        	    	parameters.put('anulada',Boolean.parseBoolean(params.anulada))			
    	        	   		detalle = DetalleServicioContratado.createCriteria().list([fetch:[lote:'eager']]){
    	            			log.debug("DENTRO DEL CLOSURE DE CONSULTA POR DETALLESERVICIO CONTRATADO")
    	        				and{
    	        					ordenReserva{
    	        						and{
    	        		    				eq('anulada',Boolean.parseBoolean(params.anulada))
    	        		    				if(params.fieldSearch=="empresa.nombre"){
    	        		    					hqlstr=hqlstr+" and o.empresa.nombre like :empresa_nombre"
    	        		    					parameters.put('empresa_nombre','%'+params.searchCriteria+'%')
    	        		        				empresa{
    	        		        					like('nombre','%'+params.searchCriteria+'%')
    	        		        				}
    	        		    				}
    	        						}
    	        					}
    	            				if(params.fieldSearch=="sector.nombre"){
    	            					hqlstr=hqlstr+" and sector.nombre like '%'+:sector_nombre+'%'"
    	            					parameters.put('sector_nombre',params.searchCriteria)
    	            					sector{
    	            						like('nombre',params.searchCriteria)
    	            					}
    	            				}
    	            				if(params.fieldSearch=="lote.nombre"){
    	            					hqlstr=hqlstr+" and lote.nombre like '%'+:lote_nombre+'%'"
    	            					parameters.put('lote_nombre',params.searchCriteria)
    	            					lote{
    	            						like('nombre',params.searchCtiteria)
    	            					}
    	            					
    	            				}
    	            		    	/*if(params.fieldSearch=='fechaalta'){
    	            		    		hqlstr=hqlstr+" and fechaAlta = :fechaalta"
    	            		    		parameters.put('fechaalta',new Date())
    	            		    		eq('fechaAlta',params.fechaalta)
    	            		    	} */   					
    	        				}
    	        			}
    	            		log.debug("antes de hacer el findAll con hql")
    	        	   		ordenes = OrdenReserva.findAll(hqlstr,parameters)
    	        			log.debug("termina de hacer un findAll con hql")
    	        			list.addAll(ordenes)
    	        			list.addAll(detalle)
    	            	}			
    	            	log.debug("TERMINO DE AGREGAR AL LIST ordenes y detalle")
    	            	if(params.sort){
    	            		if(params.sort=="nombre"){
    	           				Collections.sort(list,new EmpresaNombreComparator())
    	            		}
    	            		if(params.sort=="fechaAlta"){
    	            			Collections.sort(list,new FechaOrdenComparator())
    	            		}
    	            		if(params.sort=="lote"){
    	            			Collections.sort(list,new LoteComparator())
    	            		}
    	            		if(params.sort=="numero"){
    	            			Collections.sort(list,new NumeroOrdenComparator())
    	            		}
    	            		if(params.sort=="sector"){
    	            			Collections.sort(list,new SectorComparator())
    	            		}
    	        			if(params.dir=="DESC"){
    	        				Collections.reverse(list)
    	        				log.debug("Orden inverso aplicado")
    	        			}
    	            	}
    	            	log.debug("Cantidad de lineas Consultadas: "+list?.size())
    	            	return list
    }

    def listjson = {
    	log.info("INGRESANDO AL METODO listjson DEL CONTROLADOR OrdenReservaController")
    	log.debug("PARAMETROS $params")
    	log.debug("PARAMETRO ANULADA: "+Boolean.parseBoolean(params.anulada))
    	def list = consultar(params)
    	log.debug("Objecto list devuelo por el closure consultar: "+list.size())
    	Double totalCancelado=0
    	Double saldo=0
    	def flagdetalle = false
    	render(contentType:"text/json"){
    		rows{
    			list.each{
    				totalCancelado=0
    				saldo=0
    				if(it instanceof DetalleServicioContratado){
        				it.ordenReserva.recibos.each{ 
        					if(!it.anulado)
        						totalCancelado=totalCancelado+it.total
        				}
        				saldo=it.ordenReserva.total-totalCancelado
    					row(id:it.ordenReserva.id,numero:it.ordenReserva.numero,fechaAlta:it.ordenReserva.fechaAlta,total:it.ordenReserva.total,anio:it.ordenReserva.anio
    							,expoNombre:it.ordenReserva.expo.nombre
        						,sector:it.sector?.nombre
        						,lote:it.lote?.nombre
        						,nombre:it.ordenReserva.empresa.nombre,totalCancelado:totalCancelado,saldo:saldo)
        				
    				}else{
        				it[0].recibos.each{r-> 
        					if(!r.anulado){
        						totalCancelado=totalCancelado+r.total
        						log.debug("SALE LA PROPIEDAD TOTAL?-->"+r.total)
        					}
        				}
        				saldo=it.total[0]-totalCancelado
        				log.debug("SALDO: $saldo total cancelado: $totalCancelado")
    					row(id:it.id[0],numero:it.numero[0],fechaAlta:it.fechaAlta[0],total:it.total[0],anio:it.anio[0],expoNombre:it.expo.nombre[0]
        						,sector:""
        						,lote:""
        						,nombre:it.empresa.nombre[0],totalCancelado:totalCancelado,saldo:saldo)        				
    				}
    				

    			}
    		}
   			total list.size()
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
		
	def export = {
		log.info("INGRESANDO AL METODO exportexcel DEL CONTROLADOR OrdenReservaController")
		log.debug("PARAMETROS DE INGRESO: "+params)
		def totalOrdenes
		def totalCancelado
		def saldo
		def ordenes
		def flagdetalle
		
		List list = consultar(params)	   	
	     response.setHeader("Content-disposition", "attachment")
	     response.contentType = "application/vnd.ms-excel"
	 
	     log.debug("Cantidad registros consultados: "+list.size())
	     
	     
      	 def workbook = Workbook.createWorkbook(response.outputStream)
    	 def sheet = workbook.createSheet("Request",0)
	     
	 	 boolean falgdetalle=false
	 	 def fil=0
	 	 if(Boolean.parseBoolean(params.anulada)){
	 		 sheet.addCell(new Label(3,fil,(Boolean.parseBoolean(params.anulada)?"SOLO ORDENES ANULADAS":"") ))
	 		 fil=fil+1
	 	 }
	 	 sheet.addCell(new Label(0, fil, "Empresa"))
	 	 sheet.addCell(new Label(1, fil, "Sector"))
	 	 sheet.addCell(new Label(2, fil, "Lote"))
	 	 sheet.addCell(new Label(3, fil, "Total"))
	 	 sheet.addCell(new Label(4, fil, "Total Cancelado"))
 	 	 sheet.addCell(new Label(5, fil, "Saldo"))
	 	 sheet.addCell(new Label(6, fil, "Exposición")) 	 	 
	 	 sheet.addCell(new Label(7, fil, "Año"))
	 	 sheet.addCell(new Label(8, fil, "Número Orden"))	 	 
	 	 sheet.addCell(new Label(9, fil, "Fecha"))
		 DateFormat customDateFormat = new DateFormat ("d/m/yy h:mm") 
		 WritableCellFormat dateFormat = new WritableCellFormat (customDateFormat)                    
		 fil=fil+1
		 list.each{
    				totalCancelado=0
    				saldo=0
    				if(it instanceof DetalleServicioContratado){
	    				it.ordenReserva.recibos.each{ 
	    					if(!it.anulado)
	    						totalCancelado=totalCancelado+it.total
	    				}
	    				saldo=it.ordenReserva.total-totalCancelado
    					sheet.addCell(new Label(0,fil,it.ordenReserva.empresa.nombre))
    					sheet.addCell(new Label(1,fil,it.sector?.nombre))    					
    					sheet.addCell(new Label(2,fil,it.lote?.nombre))    					
    					sheet.addCell(new Number(3,fil,it.ordenReserva.total))
    					sheet.addCell(new Number(4,fil,totalCancelado))
    					sheet.addCell(new Number(5,fil,saldo))
    					sheet.addCell(new Label(6,fil,it.ordenReserva.expo.nombre))
    					sheet.addCell(new Number(7,fil,it.ordenReserva.anio))
    					sheet.addCell(new Number(8,fil,it.ordenReserva.numero))
    					sheet.addCell(new DateTime (9,fil,it.ordenReserva.fechaAlta,dateFormat))
    					fil=fil+1    					    					    					    					    					    					    					
    				}else{
    					it[0].recibos.each{r->
    						if(!r.anulado)
    							totalCancelado=totalCancelado+r.total
    					}
    					saldo=it.total[0]-totalCancelado
    					sheet.addCell(new Label(0,fil,it.empresa.nombre[0]))
    					sheet.addCell(new Label(1,fil,""))    					
    					sheet.addCell(new Label(2,fil,""))    					
    					sheet.addCell(new Number(3,fil,it.total[0]))
    					sheet.addCell(new Number(4,fil,totalCancelado)) 
    					sheet.addCell(new Number(5,fil,saldo))
    					sheet.addCell(new Label(6,fil,it.expo.nombre[0]))
    					sheet.addCell(new Number(7,fil,it.anio[0]))
    					sheet.addCell(new Number(8,fil,it.numero[0]))
    					sheet.addCell(new DateTime (9,fil,it.fechaAlta[0],dateFormat))
    					fil=fil+1    					    					    					    					    					    					    					
    				}
    				
    	}
      	workbook.write()  
	    workbook.close()  	
    }
    
}
