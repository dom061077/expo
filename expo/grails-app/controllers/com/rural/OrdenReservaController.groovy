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
		def errorsList=[]   
   		try{
			ordenReservaInstance=ordenReservaService.generarOrdenReserva(ordenReservaInstance,empresaInstance)
			render(contentType: "text/json"){
				success true
				ordenid ordenReservaInstance.id
			}
		}catch(OrdenReservaException e){
			if(e.ordenReserva.hasErrors()){
				e.ordenReserva.errors.allErrors.each {error->
					error.codes.each{
						if(g.message(code:it)!=it)
							errorsList.add(g.message(code:it))
					}
				}	
			}else{
				if(e.ordenReserva.empresa.hasErrors()){
					e.ordenReserva.empresa.errors.allErrors.each{error->
						error.codes.each{
							if(g.message(code:it)!=it)
								errorsList.add(g.message(code:it))
						}
					}
				}else{
					errorsList.add(e.message)
				}
			}
			render(contentType: "text/json"){			
				success false
				errors{
					errorsList.each { 
						title it
					}
				}
			}
		}   	
    }
    
    def ordenreservareporte = {
    	log.info("INGRESANDO AL METODO ordenreservareporte DEL CONTROLADOR OrdenReservaController")
    	log.debug("PARAMETROS ENVIADOS: "+params)
    	def ordenReservaInstance = OrdenReserva.get(params.id)
    	log.debug(ordenReservaInstance.empresa.nombre)
    	log.debug(ordenReservaInstance.empresa.vendedor.nombre)
    	log.debug "SUBTOTAL DE ORDEN DE RESERVA: "+ordenReservaInstance.subtotalDetalle
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
    
	

	
	List consultar2(params){
		log.info "DENTRO DEL METODO consultar2"
		def ordenes=null
		def detalles=null
		def i
		String valorSearch
		String condicion
		String campo
		ArrayList list = new ArrayList()
		/*consulto las ordenes que no tienen detalle*/
		java.text.DateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy")	 
		ordenes=OrdenReserva.createCriteria().list(){

			for(i = 0; i<params.campos.size();i++){
					valorSearch = params.searchString[i]
					condicion = params.condiciones[i]
					if(condicion.trim().equals("ilike2")){
						condicion="ilike"
						valorSearch="%"+valorSearch+"%"
					}
					campo = params.campos[i]
					log.debug "CAMPO: $campo"
					if(campo.trim().equals("fechaAlta")){
						try{
							valorSearch = df.parse(valorSearch)		
							log.debug "FECHA CONSULTADA 1: $valorSearch TIPO: "+valorSearch.class
						}catch(Exception e){
							log.debug "EXCEPCION LANZADA "+e
							valorSearch = new Date()
						}
						
					}else{
						log.debug "LA CONDICION DEL CAMPO FECHAALTA INGRESA POR EL ELSE"
					}
					
					and{
						isEmpty("detalle")

						if(!campo.trim().equals("") && !condicion.trim().equals("")
							&& !valorSearch.trim().equals("")){
								
									log.debug "CUMPLE CON LAS CONDICIONES DE CAMPO,CONDICION Y VALOR VACIO $campo, $condicion, $valorSearch"
									
							
									if(campo.trim().equals("nombre")){
										empresa{
											"${condicion}" ("nombre",valorSearch)
										}
									}else{
										if(campo.trim().equals("expo")){
											expo{
											   "${condicion}"("nombre",valorSearch)
											}
										}else{
											if(campo.trim().equals("sector")){

												detalle{
													sector{	
														"${condicion}" ("nombre",valorSearch)
														
													}
												}
											}else{
												if(campo.trim().equals("lote")){
													detalle{
														lote{
															"${condicion}"("nombre",valorSearch)
														}
													}	
												}else{
												
													if(campo.equals("anulada")){
														
													
														if(valorSearch.trim().equals("SI"))
															"${condicion}"(campo,true)
														else
															"${condicion}"(campo,false)
													}else
														log.debug "ANTES DE AGREGAR EL FILTRO DE FINAL"
														"${condicion}"(campo,valorSearch)
													
												}
											}
										}
									}
									
						}
							
					 }
				}

		}//llave de cierre del list
		
		list.addAll(ordenes)
		log.debug "CANTIDAD DE ORDENES: "+ordenes.size()
		/*consulto las ordenes que tienen detalle*/
		detalles = DetalleServicioContratado.createCriteria().list([fetch:[lote:'eager']]){

			for(i = 0; i<params.campos.size();i++){

				valorSearch = params.searchString[i]
				condicion = params.condiciones[i]
				if(condicion.trim().equals("ilike2")){
					condicion="ilike"
					valorSearch="%"+valorSearch+"%"
				}
				campo = params.campos[i]
				if(campo.equals("anulada")){
				    valorSearch=false
 					if(valorSearch.equals("SI"))
				        valorSearch=true
				
				}
				
				if(campo.trim().equals("fechaAlta")){
					try{
						valorSearch = df.parse(valorSearch)		
						log.debug "FECHA CONSULTADA2: $valorSearch"
					}catch(Exception e){
						valorSearch = new Date()
					}
					
				}				
				
				and{
					if(!campo.trim().equals("") && !condicion.trim().equals("")
						&& !valorSearch.trim().equals("")){
						
						if(campo.trim().equals("nombre")){
							ordenReserva{
								empresa{
									"${condicion}" ("nombre",valorSearch)
									
								}
							}
						}
						
						if(campo.trim().equals("sector")){
							sector{
								"${condicion}"("nombre",valorSearch)
								
							}
						}
						
						if(campo.equals("expo") || campo.equals("numero") || campo.trim().equals("anulada")){
						   ordenReserva{
								   if(campo.trim().equals("expo")){
									   expo{
										   "${condicion}"("nombre",valorSearch)
										   
									   }
								   }else{
										if(campo.equals("anulada")){
											if(valorSearch.trim().equals("SI"))
												"${condicion}"(campo,true)
											else
												"${condicion}"(campo,false)
										}else
											"${condicion}"(campo,valorSearch)
						   			}
					   
					   		} 
				   		}
				   }//end del if de condicion de condicion, campo y valorSearch no vacio
				 }  
			}
		
		}
		log.debug "CANTIDAD DE DETALLES: "+detalles.size()
		list.addAll(detalles)
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
		
		
		
		return list
		
		
	}
    

    List  consultar(params){
    	log.debug("Dentro del metodo consultar")
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
    	            	def i
    	            	String valorSearch
    	            	String condicion
    	            	String campo 
    	            	hqlstr="from OrdenReserva o left outer join o.detalle det where det is null "
    	            	
    	        			
	        	   		detalle = DetalleServicioContratado.createCriteria().list([fetch:[lote:'eager']]){
	            				log.debug("DENTRO DEL CLOSURE DE CONSULTA POR DETALLESERVICIO CONTRATADO")
		    	            	for(i = 0; i<params.campos.size();i++){

		    	            		valorSearch = params.searchString[i]
		    	            		condicion = params.condiciones[i]
		    	            		campo = params.campos[i]
		    	            		and{
			    	            		if(!campo.trim().equals("") && !condicion.trim().equals("")
			    	            			&& !valorSearch.trim().equals("")){
											if(campo.trim().equals("nombre")){
												ordenReserva{
													empresa{
														"${condicion}" ("nombre",valorSearch)
														hqlstr=hqlstr+" and empresa.nombre $condicion $valorSearch"
													}
												}		    	            				
											}
											if(campo.trim().equals("sector")){
												sector{
													condicion("nombre",valorSearch)
													hqlstr=hqlstr+" and sector.nombre $condicion $valorSearch "
												}
											}
											if(campo.equals("expo") || campo.equals("numero") || campo.trim().equals("anulada")){
											   ordenReserva{
											   		if(campo.trim().equals("expo")){
											   			expo{
											   				condicion("nombre",valorSearch)
											   				hqlstr=hqlstr+" and expo.nombre $condicion $valorSearch"
											   			}
											   		}else{
										   				condicion(params.campos[i],valorSearch)
										   				hqlstr=hqlstr+" and $campo $condicion $valorSearch"
											   		}
											   				
											   }
										   }	
			   	            			}
			   	            		}	
		    	            	}
	            			
	        			}
	            		log.debug("antes de hacer el findAll con hql")
	        	   		//ordenes = OrdenReserva.findAll(hqlstr,parameters)
	        			log.debug("termina de hacer un findAll con hql")
	        			list.addAll(ordenes)
	        			list.addAll(detalle)
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
    	def list =  consultar2(params)
    	
    	
    	log.debug("Objecto list devuelo por el closure consultar: "+list?.size())
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
								,subTotal:it.subTotal
        						,sector:it.sector?.nombre
        						,lote:it.lote?.nombre
        						,nombre:it.ordenReserva.empresa.nombre,totalCancelado:totalCancelado,saldo:saldo)
        				
    				}else{
						
        				it.recibos.each{r-> 
        					if(!r.anulado){
        						totalCancelado=totalCancelado+r.total
        					}
        				}
        				saldo=it.total-totalCancelado
    					row(id:it.id,numero:it.numero,fechaAlta:it.fechaAlta,total:it.total,anio:it.anio,expoNombre:it.expo.nombre
        						,sector:""
								,subTotal:0
        						,lote:""
        						,nombre:it.empresa.nombre,totalCancelado:totalCancelado,saldo:saldo)   
						     				
    				}
    				

    			}
    		}
   			total list?.size()
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
		
		List list = consultar2(params)	   	
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
	 	 sheet.addCell(new Label(0, fil, "Nombre Comercial"))
	 	 sheet.addCell(new Label(1, fil, "Sector"))
	 	 sheet.addCell(new Label(2, fil, "Lote"))
	 	 sheet.addCell(new Label(3, fil, "Total"))
	 	 sheet.addCell(new Label(4, fil, "Total Cancelado"))
 	 	 sheet.addCell(new Label(5, fil, "Saldo"))
	 	 sheet.addCell(new Label(6, fil, "Exposición")) 	 	 
	 	 sheet.addCell(new Label(7, fil, "Año"))
	 	 sheet.addCell(new Label(8, fil, "Número Orden"))	 	 
	 	 sheet.addCell(new Label(9, fil, "Fecha"))
		 sheet.addCell(new Label(10,fil, "Razon Social"))
		 sheet.addCell(new Label(11,fil, "C.U.I.T"))
		 sheet.addCell(new Label(12,fil, "Direccion")) 
		 sheet.addCell(new Label(13,fil, "E-mail"))
		 sheet.addCell(new Label(14,fil, "Nombre Representante"))
		 sheet.addCell(new Label(15,fil, "D.N.I Representante"))
		 sheet.addCell(new Label(16,fil, "Telefono 1 Representante"))
		 sheet.addCell(new Label(17,fil, "Telefono 2 Representante"))
		 sheet.addCell(new Label(18,fil, "Telefono 3 Representante"))
		 sheet.addCell(new Label(19,fil, "Sitio Web"))
		 sheet.addCell(new Label(20,fil, "Rubro"))
		 sheet.addCell(new Label(21,fil, "Sub-Rubro"))
		 sheet.addCell(new Label(22,fil, "Vendedor"))
		 sheet.addCell(new Label(23,fil, "Provincia"))
		 sheet.addCell(new Label(24,fil, "Localidad"))
		 sheet.addCell(new Label(25,fil, "Observacion"))
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
    					sheet.addCell(new DateTime(9,fil,it.ordenReserva.fechaAlta,dateFormat))
						sheet.addCell(new Label(10,fil,it.ordenReserva.empresa.razonSocial))
						sheet.addCell(new Label(11,fil,it.ordenReserva.empresa.cuit))
						sheet.addCell(new Label(12,fil,it.ordenReserva.empresa.direccion))
						sheet.addCell(new Label(13,fil,it.ordenReserva.empresa.email))
						sheet.addCell(new Label(14,fil,it.ordenReserva.empresa.nombreRepresentante))
						sheet.addCell(new Label(15,fil,it.ordenReserva.empresa.dniRep))
						sheet.addCell(new Label(16,fil,it.ordenReserva.empresa.telefonoRepresentante1))
						sheet.addCell(new Label(17,fil,it.ordenReserva.empresa.telefonoRepresentante2))
						sheet.addCell(new Label(18,fil,it.ordenReserva.empresa.telefonoRepresentante3))
						sheet.addCell(new Label(19,fil,it.ordenReserva.empresa.sitioWeb))
						sheet.addCell(new Label(20,fil,it.ordenReserva.empresa.subrubro?.rubro?.nombreRubro))
						sheet.addCell(new Label(21,fil,it.ordenReserva.empresa.subrubro?.nombreSubrubro))
						sheet.addCell(new Label(22,fil,it.ordenReserva.empresa.vendedor.nombre))
						sheet.addCell(new Label(23,fil,it.ordenReserva.empresa.localidad?.provincia?.nombre))
						sheet.addCell(new Label(24,fil,it.ordenReserva.empresa.localidad?.nombreLoc))
						sheet.addCell(new Label(25,fil,it.ordenReserva.observacion))
						
    					fil=fil+1    					    					    					    					    					    					    					
    				}else{
    					it.recibos.each{r->
    						if(!r.anulado)
    							totalCancelado=totalCancelado+r.total
    					}
    					saldo=it.total-totalCancelado
    					sheet.addCell(new Label(0,fil,it.empresa.nombre))
    					sheet.addCell(new Label(1,fil,""))    					
    					sheet.addCell(new Label(2,fil,""))    					
    					sheet.addCell(new Number(3,fil,it.total))
    					sheet.addCell(new Number(4,fil,totalCancelado)) 
    					sheet.addCell(new Number(5,fil,saldo))
    					sheet.addCell(new Label(6,fil,it.expo.nombre))
    					sheet.addCell(new Number(7,fil,it.anio))
    					sheet.addCell(new Number(8,fil,it.numero))
    					sheet.addCell(new DateTime (9,fil,it.fechaAlta,dateFormat))
    					fil=fil+1    					    					    					    					    					    					    					
    				}
    				
    	}
      	workbook.write()  
	    workbook.close()  	
    }
    
}
