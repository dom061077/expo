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
import com.rural.utils.ExpoNombreComparator
import com.rural.utils.GUtilDomainClass
import org.codehaus.groovy.grails.commons.DefaultGrailsDomainClass;
import com.rural.utils.FilterUtils;
import java.text.SimpleDateFormat
import java.sql.Date 
import java.util.ArrayList

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
	def grailsApplication
    
    def index = { redirect(action:list,params:params) }
    	

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [delete:'POST', save:'POST', update:'POST']

    def list = {
        params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
		Random randomLink = new Random()
        [ ordenReservaInstanceList: OrdenReserva.list( params ), ordenReservaInstanceTotal: OrdenReserva.count(),randomlink:randomLink.nextInt(100000) ]
    }

    def showtorecibo = {
    	log.info("INGRESANDO AL METODO showtorecbo DEL CONTROLADOR OrdenReservaController")
    	log.debug("PARAMETROS $params")
    	def ordenReserva = OrdenReserva.get(params.id)
    	/*ordenReserva.recibos.each{
    		if(!it.anulado)
    			saldo = saldo - it.total
    	}*/
		def saldo =ordenReserva.total - (ordenReserva.credito==null?0:ordenReserva.credito) - (ordenReserva.recibo==null?0:ordenReserva.recibo) + (ordenReserva.debito==null?0:ordenReserva.debito)
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
		Random randomLink = new Random()
        return [randomlink:randomLink.nextInt(100000)]
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
    	ordenReservaInstance.detalle.each{
    		log.debug(it)
    		log.debug(it.sector?.nombre)
    		log.debug(it.lote?.nombre)
			it.descuentos.each{ descuento ->
				log.debug "${descuento.porcentaje}"
			}
    	}
    	ordenReservaInstance.otrosconceptos.each{
    		log.debug(it.subTotal)
    	}
    	ordenReservaInstance.productos.each{
    		log.debug(it)
    	}
    	log.debug(ordenReservaInstance.expo.nombre)
    	log.debug(ordenReservaInstance.usuario.userRealName)
    	log.debug(ordenReservaInstance.localidad?.nombreLoc)
    	log.debug(ordenReservaInstance.localidad?.provincia?.nombre)
    	List ordenList = new ArrayList()
    	ordenList.add(ordenReservaInstance)
    	ordenList.add(ordenReservaInstance)    	
    	ordenList.add(ordenReservaInstance)
		//-------recuperacion de desde la instancia de logo correspondiente a la exposicion y al año de la Orden
		def listlogos = Logo.createCriteria().list(){
			and{
				expo{
					eq("id",ordenReservaInstance.expo.id)
				}
				eq("anio",ordenReservaInstance.anio)
			}
		}	
		def logo
		listlogos.each{
			logo = it
		}
		//------------------------------------------------------------------------------------    	

		String pathtofile = servletContext.getRealPath("/reports/images")+"/"+ordenReservaInstance.expo.nombre.trim()+ordenReservaInstance.anio+".jpg"
		if(logo?.image){
			FileOutputStream foutput = new FileOutputStream(new File(pathtofile))
			foutput.write(logo?.image)
			foutput.flush()
		}
    	
    	
    	log.debug("Orden Reserva: $ordenReservaInstance")
		String reportsDirPath = servletContext.getRealPath("/reports/");
		params.put("reportsDirPath", reportsDirPath);
		params.put("logo",ordenReservaInstance.expo.nombre.trim()+ordenReservaInstance.anio+".jpg")
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
	
	def parseValue(/*def prop, def paramName,*/ def rawValue, def mp, def params, def condicion) {
    	//def mp = FilterUtils.getNestedMetaProperty(mc, prop)
		
        //log.debug("prop is ${prop}")
        //log.debug("mc is ${mc}, mc class is ${mc.theClass.name}")
        //log.debug("mp is ${mp}, name is ${mp.name} and type is ${mp.type} and is enum is ${mp.type.isEnum()}")
    	def val = rawValue
        //log.debug("cls is ${cls}")

        if (val) {
            Class cls = mp.type //mc.theClass.getDeclaredField(prop).type
            if (cls.isEnum()) {
                val = Enum.valueOf(cls, val.toString())
                //println "val is ${val} and raw val is ${rawValue}"
            } else if (mp.type.getSimpleName().equalsIgnoreCase("boolean")) {
				try{
					val = val.toBoolean()
				}catch(Exception e){
					val = false
				}
            } else if (mp.type == Integer || mp.type == int) {
				try{
					val = val.toInteger()
				}catch(Exception e){
					val = "0".toInteger()
				}
            } else if (mp.type == Long || mp.type == long) {
				try{
					val = val.toLong()
				}catch(Exception e){
					val = "0".toLong()
				}
            } else if (mp.type == Double || mp.type == double) {
				try{
					val = val.toDouble()
				}catch(Exception e){
					val = "0".toDouble()
				}
            } else if (mp.type == Float || mp.type == float) {
				try{
					val = val.toFloat()
				}catch(Exception e){
					val = "0".toFloat()
				}
            } else if (mp.type == Short || mp.type == short) {
				try{
					val = val.toShort()
				}catch(Exception e){
					val = "0".toShort()
				}
            } else if (mp.type == BigDecimal) {
				try{
					val = val.toBigDecimal()
				}catch(Exception e){
					val = "0".toBigDecimal()
				}
            } else if (mp.type == BigInteger) {
				try{
					val = val.toBigInteger()
				}catch(Exception e){
					val = "0".toBigInteger()
				}
            } else if (java.sql.Date.isAssignableFrom(mp.type)) {
          		def df
				try{
					df=new SimpleDateFormat("dd/MM/yyyy")
					val= df.parse(val)
					log.debug "FECHA PARSEADA: "+val
				}catch(Exception e){
					log.debug "LA FECHA NO SE PUDO PARSEAR"
					log.debug "ERROR DE FECHA: "+e.message
					val=new Date()
				}
            }else{
				if(condicion.equals("ilike") || condicion.equals("ilike2"))
					val= "%${val}%"
            }
        }
    	//println "== Parsing value ${rawValue} from param ${paramName}. type is ${mp.type}.  Final value ${val}. Type ${val?.class}"
    	return val
    }

	


	
	
	List consultar2(params){
		def ordenes=null
		def detalles=null
		def i
		def valorSearch
		String condicion
		String campo
		def campoToken
		ArrayList listgral = new ArrayList()
		
		/*consulto las ordenes que no tienen detalle*/
		java.text.DateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy")
		java.util.Date fecha
		java.sql.Date fechasql
		def gc
		def flagnot=false
		
		def filtros 
		
		try{
			filtros = JSON.parse(params.filter)
		}catch(Exception e){
		
		}
		
		filtros.each{
			log.debug "FILTRO: ${it}"
		}
		
		
		def co = OrdenReserva.createCriteria()
		
		
		if(filtros){		
			ordenes=co.list({
						//def metaProperty
						and{
							co.isEmpty("detalle")
							co.eq("anulada",Boolean.parseBoolean(params.soloanuladas))
							filtros.each{filtro->
								log.debug "FILTRO: ${filtro["field"]}"
								//[field:nombre, value:oooo, type:string]
								if(filtro["field"].equals("sector") || filtro["field"].equals("lote")){
									co.isNotEmpty("detalle")
								}
								if( filtro["field"].equals("nombre")){
									log.debug "LOGRO INGRESAR POR LA CONDICION DE NOMBRE"
									co.ilike(filtro["field"],"%"+filtro["value"]+"%")
								}
								
								if(filtro["field"].equals("razonSocial")){
									log.debug "LOGRO INGRESAR POR LA CONDICION DE NOMBRE"
									co.ilike(filtro["field"],"%"+filtro["value"]+"%")
								}
	
								
								if(filtro["field"].equals("numero")){
									co."${filtro["comparison"]}"(filtro["field"],filtro["value"].toLong())
								}
								if(filtro.field.equals("anio")){
									co."${filtro["comparison"]}"(filtro["field"],filtro["value"].toInteger())
								}
								if(filtro["field"].equals("fechaAlta")){
									fecha = df.parse(filtro["value"])
									fechasql = new java.sql.Date(fecha.getTime())
									
									log.debug "FECHA FORMATEADA: ${fecha} desde ${filtro["value"]}"
									co."${filtro.comparison}"(filtro["field"],fechasql)
								}
								
								if(filtro["field"].equals("usuario")){
									co.usuario{
										co.ilike("userRealName","%"+filtro["value"]+"%")
									}
								}
								
								if(filtro["field"].equals("vendedor")){
									co.vendedor{
										co.ilike("nombre","%"+filtro["value"]+"%")
									}
								}
								
								
	
								co.expo{
									if(filtro["field"].equals("expoNombre")){
										co.ilike("nombre","%"+filtro["value"]+"%")
									}
								}
							}
					
						}
						/*for(i=0;i<params.campos?.size()-1;i++){
							campo=params.campos[i]
							log.debug "Condiciones: "+params.condiciones
							condicion=params.condiciones[i]
							valorSearch=params.searchString[i]
							and{
								co.isEmpty("detalle")
								co.eq("anulada", Boolean.parseBoolean(params.soloanuladas) )
	
								if(!campo?.trim().equals("") && !condicion.trim().equals("") && !valorSearch.trim().equals("")){
											metaProperty=FilterUtils.getNestedMetaProperty(grailsApplication,OrdenReserva,campo)
											if(campo?.contains(".")){
												campoToken=campo?.tokenize(".")
												log.debug "POR QUE ESTA TRAYENDO ALGO QUE PARECE NULL: ${metaProperty}"
												if(!metaProperty){
													
													metaProperty=FilterUtils.getNestedMetaProperty(grailsApplication,DetalleServicioContratado,campo)
													valorSearch=parseValue(valorSearch,metaProperty,params,condicion)
													co.detalle(){	
														co."${campoToken[0]}"(){
															if(condicion.equals("ilike2"))
																co.not{
																	co.ilike(campoToken[1],valorSearch)	
																}
															else
																co."${condicion}"(campoToken[1],valorSearch)
																
														}
													}
												}else{
													log.debug "META PROPERTY ENCONTRADA ${metaProperty}"
													valorSearch=parseValue(valorSearch,metaProperty, params,condicion)
													log.debug "CampoToken[0]: ${campoToken[0]} CampoToken[1]: ${campoToken[1]}, valorSearch:${valorSearch}"
													co."${campoToken[0]}"(){
														if(condicion.equals("ilike2"))
															co.not{
																co.ilike(campoToken[1],valorSearch)
															}
														else
															co."${condicion}"(campoToken[1],valorSearch) 
													}
												}
											}else{
												log.debug "INGRESA POR EL ELSE DEBIDO A QUE EL CAMPO NO ES ANIDADO: campo: ${campo}, condicion: ${condicion}"
												valorSearch=parseValue(valorSearch,metaProperty,params,condicion)
												if(condicion.equals("ilike2"))
													co.not{
															co.ilike campo, valorSearch
														}
												else
													co."${condicion}"(campo,valorSearch)
											}
											
								}
							}//end del and
						}//end del for
						*/
						
				})//end de co.list
				listgral.addAll(ordenes)
			}//end del if filters
		
		
		log.debug "CANTIDAD DE ORDENES DESDE LA CABECERA: ${ordenes?.size()}"
		def cd=DetalleServicioContratado.createCriteria()
		def closureDetalle={
			log.debug "CLOSURE DE DETALLE"
			def metaProperty
			and{
				cd.ordenReserva{
					cd.eq("anulada",Boolean.parseBoolean(params.soloanuladas))
				}
				filtros.each{filtro->
					log.debug "FILTRO: ${filtro}"
					//[field:nombre, value:oooo, type:string]
					
					cd.ordenReserva{
						
						if(filtro["field"].equals("nombre")){
							cd.ilike(filtro["field"],"%"+filtro["value"]+"%")
						}
						if(filtro["field"].equals("razonSocial")){
							log.debug "LOGRO INGRESAR POR LA CONDICION DE NOMBRE"
							cd.ilike(filtro["field"],"%"+filtro["value"]+"%")
						}

						if(filtro["field"].equals("numero")){
							cd."${filtro["comparison"]}"(filtro["field"],filtro["value"].toLong())
						}
						if(filtro["field"].equals("anio")){
							cd."${filtro["comparison"]}"(filtro["field"],filtro["value"].toInteger())
						}
						if(filtro["field"].equals("fechaAlta")){
							fecha = df.parse(filtro["value"])
							log.debug "FECHA FORMATEADA: ${fecha} desde ${filtro["value"]}"
							cd."${filtro["comparison"]}"(filtro["field"],fecha)
						}

						cd.expo{
							if(filtro["field"].equals("expoNombre")){
								cd.ilike("nombre","%"+filtro["value"]+"%")
							}
						}
						if(filtro["field"].equals("usuario")){
							cd.usuario{
								ilike("userRealName","%"+filtro["value"]+"%")
							}
						}
						if(filtro["field"].equals("vendedor")){
							cd.vendedor{
								ilike("nombre","%"+filtro["value"]+"%")
							}
						}


					}
					if(filtro["field"].equals("sector")){
						cd.sector{
							cd.ilike("nombre","%"+filtro["value"]+"%")
						}
					}

					if(filtro["field"].equals("lote")){
						cd.lote{
							cd.ilike("nombre","%"+filtro["value"]+"%")
						}
					}

					
				}
			}
			/*for(i=0;i<params.campos?.size()-1;i++){
				campo=params.campos[i]
				condicion=params.condiciones[i]
				valorSearch=params.searchString[i]
				and{
					cd.ordenReserva(){
						cd.eq("anulada", Boolean.parseBoolean(params.soloanuladas) )
					}
					if(!campo.trim().equals("") &&  !condicion.trim().equals("") && !valorSearch.trim().equals("")){
								metaProperty=FilterUtils.getNestedMetaProperty(grailsApplication,DetalleServicioContratado,campo)
								if(campo?.contains(".")){
									campoToken=campo.tokenize(".")
									log.debug "metaProperty en el closure detalle: ${metaProperty} campo utilizado: ${campo}"
									if(!metaProperty){
										metaProperty=FilterUtils.getNestedMetaProperty(grailsApplication,OrdenReserva,campo)
										if(metaProperty)
										valorSearch=parseValue(valorSearch,metaProperty, params,condicion)
										cd.ordenReserva(){
											cd."${campoToken[0]}"(){
												if(condicion.equals("ilike2"))
													cd.not{
														cd.ilike (campoToken[1],valorSearch)
													}
												else
													cd."${condicion}"(campoToken[1],valorSearch)
											}
										}
									}else{
										//metaclass=FilterUtils.getNestedMetaProperty(DetalleServicioContrado.getMetaClass(),campo)
										valorSearch=parseValue(valorSearch,metaProperty,params,condicion)
										cd."${campoToken[0]}"(){
											if(condicion.equals("ilike2"))
												cd.not{
													cd.ilike (campoToken[1],valorSearch)
												}
											else
												cd."${condicion}"(campoToken[1],valorSearch)
										}
									}
								}else{
									if(metaProperty){
										valorSearch=parseValue(valorSearch,metaProperty,params,condicion)
										if(condicion.equals("ilike2"))
											cd.not{
												cd.ilike (campo,valorSearch)
											}
										else
											cd."${condicion}"(campo,valorSearch)
									}else{
										metaProperty=FilterUtils.getNestedMetaProperty(grailsApplication,OrdenReserva,campo)
										valorSearch=parseValue(valorSearch,metaProperty,params,condicion)
										cd.ordenReserva{
											if(condicion.equals("ilike2"))
												cd.not{
													cd.ilike(campo,valorSearch)
												}
											else
												cd."${condicion}"(campo,valorSearch)
										}
									}
								}
					}
				}//end del and
			}//end del for*/
			
		}//end del closureDetalle
		if(filtros){
			detalles=cd.list(closureDetalle)
			listgral.addAll(detalles)
			
		}
		log.debug "CANTIDAD DE ORDENES DESDE EL DETALLE: ${detalles?.size()}"

		if(params.sort){
			if(params.sort=="nombre"){
				   Collections.sort(listgral,new EmpresaNombreComparator())
				   log.debug "ORDENAMIENTO SOBRE NOMBRE DE EMPRESA"
			}
			if(params.sort=="fechaAlta"){
				Collections.sort(listgral,new FechaOrdenComparator())
				log.debug "ORDENEAMIENTO SOBRE FECHA DE ALTA"
			}
			if(params.sort=="lote"){
				Collections.sort(listgral,new LoteComparator())
				log.debug "ORDENAMIENTO SOBRE LOTE"
			}
			if(params.sort=="numero"){
				Collections.sort(listgral,new NumeroOrdenComparator())
				log.debug "ORDENAMIENTO SOBRE NUMERO DE ORDEN"
			}
			if(params.sort=="sector"){
				Collections.sort(listgral,new SectorComparator())
				log.debug "ORDENAMIENTO SOBRE SECTOR"
			}
			
			if(params.sort=="expoNombre"){
				Collections.sort(listgral,new ExpoNombreComparator())
				log.debug "ORDENAMIENTO SOBRE EL NOMBRE DE EXPOSICION"
			}

			if(params.dir=="DESC"){
				Collections.reverse(listgral)
				log.debug("Orden inverso aplicado")
			}
			
			
		}

		
		log.debug "TOTAL DE REGISTROS DE listgral: "+listgral.size()	
		return listgral
	}//cierre del closure principal

	
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
    	def list = consultar2(params)
    	log.debug("Objecto list devuelo por el closure consultar: "+list.size())
    	Double totalCancelado=0
    	Double saldoOrd=0
		ArrayList cabecerasMostradas = new ArrayList()
    	def flagdetalle = false
		def mostrarTotal = false
    	render(contentType:"text/json"){
    		rows{
    			list?.each{
    				totalCancelado=0
					
    				
    				if(it instanceof DetalleServicioContratado){
						if(cabecerasMostradas.indexOf(it.ordenReserva)<0){
							cabecerasMostradas.add(it.ordenReserva)
							mostrarTotal=true
						}else{
							mostrarTotal=false
						}
								
								
						
						
        				it.ordenReserva.recibos.each{ 
        					if(!it.anulado)
        						totalCancelado=totalCancelado+it.total
        				}
        				//saldoOrd = it.ordenReserva.total - it.ordenReserva.credito - it.ordenReserva.recibo + it.ordenReserva.debito
    					row(id:it.id+1000,ordenId:it.ordenReserva.id,numero:it.ordenReserva.numero
								,fechaAlta:it.ordenReserva.fechaAlta
								,total:(mostrarTotal?it.ordenReserva.total: 0)
								,anio:it.ordenReserva.anio
    							,expoNombre:it.ordenReserva.expo.nombre
								,subTotal:(mostrarTotal?it.subTotal:0)
								,subTotalOtrosConceptos:(mostrarTotal?it.ordenReserva.subtotalOtrosConceptos:0)
								,debito:(mostrarTotal?it.ordenReserva.debito:0)									
								,credito:(mostrarTotal?it.ordenReserva.credito:0)
								,recibo:(mostrarTotal?it.ordenReserva.recibo:0)
								,saldo:(mostrarTotal?saldoOrd:0)
        						,sector:(it.sector==null?'':it.sector.nombre)
        						,lote: (it.lote==null?'':it.lote.nombre)
        						,nombre:it.ordenReserva.nombre
								,razonSocial:it.ordenReserva.razonSocial
								,usuario: it.ordenReserva.usuario.userRealName
								,vendedor: it.ordenReserva.vendedor.nombre)
        				
    				}else{
						if(cabecerasMostradas.indexOf(it)<0){
							cabecerasMostradas.add(it)
							mostrarTotal=true	
						}else{
							mostrarTotal=false
						}
        				it.recibos.each{r-> 
        					if(!r.anulado){
        						totalCancelado=totalCancelado+r.total
        					}
        				}
        				//saldoOrd=it.total - it.credito - it.recibo + it.debito
    					row(id:it.id,ordenId:it.id,numero:it.numero,fechaAlta:it.fechaAlta
								,total:(mostrarTotal?it.total:0)
								,anio:it.anio,expoNombre:it.expo.nombre
        						,sector:""
								,subTotal:0
								,subTotalOtrosConceptos:(mostrarTotal?it.subtotalOtrosConceptos:0)
								,debito:(mostrarTotal?it.debito:0)
								,credito:(mostrarTotal?it.credito:0)
								,recibo:(mostrarTotal?it.recibo:0)
								,saldo:(mostrarTotal?saldoOrd:0)
        						,lote:""
        						,nombre:it.nombre
								,razonSocial:it.razonSocial
								,usuario: it.usuario.userRealName
								,vendedor: it.vendedor.nombre)

								       				
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
		ArrayList cabecerasMostradas = new ArrayList()
		def mostrarTotal = false
		
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
		 sheet.addCell(new Label(0, fil, "Usuario"))
		 sheet.addCell(new Label(1, fil, "Vendedor"))
	 	 sheet.addCell(new Label(2, fil, "Nombre Comercial"))
	 	 sheet.addCell(new Label(3, fil, "Sector"))
	 	 sheet.addCell(new Label(4, fil, "Lote"))
		 sheet.addCell(new Label(5, fil, "Sub-Total"))
		 sheet.addCell(new Label(6, fil, "\$ Otros Conceptos"))
		 sheet.addCell(new Label(7, fil, "\$ Total"))
	 	 sheet.addCell(new Label(8, fil, "\$ Debitos"))
		 sheet.addCell(new Label(9, fil, "\$ Creditos"))
		 sheet.addCell(new Label(10, fil, "\$ Recibos"))
	 	 sheet.addCell(new Label(11, fil, "Saldo"))
 	 	 sheet.addCell(new Label(12, fil, "Exposición"))
	 	 sheet.addCell(new Label(13, fil, "Año")) 	 	 
	 	 sheet.addCell(new Label(14, fil, "Número de Orden"))
	 	 sheet.addCell(new Label(15, fil, "Fecha Alta"))	 	 
	 	 sheet.addCell(new Label(16, fil, "Razón Social"))
		 sheet.addCell(new Label(17,fil, "C.U.I.T"))
		 sheet.addCell(new Label(18,fil, "Dirección"))
		 sheet.addCell(new Label(19,fil, "E-mail")) 
		 sheet.addCell(new Label(20,fil, "Nombre Representante"))
		 sheet.addCell(new Label(21,fil, "D.N.I Representante"))
		 sheet.addCell(new Label(22,fil, "Telefono 1 Representante"))
		 sheet.addCell(new Label(23,fil, "Telefono 2 Representante"))
		 sheet.addCell(new Label(24,fil, "Telefono 3 Representante"))
		 sheet.addCell(new Label(25,fil, "Sitio Web"))
		 sheet.addCell(new Label(26,fil, "Rubro"))
		 sheet.addCell(new Label(27,fil, "Sub-Rubro"))
		 sheet.addCell(new Label(28,fil, "Vendedor"))
		 sheet.addCell(new Label(29,fil, "Provincia"))
		 sheet.addCell(new Label(30,fil, "Localidad"))
		 sheet.addCell(new Label(31,fil, "Observacion"))
		 DateFormat customDateFormat = new DateFormat ("d/m/yy h:mm") 
		 WritableCellFormat dateFormat = new WritableCellFormat (customDateFormat)                    
		 fil=fil+1
		 list.each{
    				totalCancelado=0
    				saldo=0
    				if(it instanceof DetalleServicioContratado){
	    				/*it.ordenReserva.recibos.each{ 
	    					if(!it.anulado)
	    						totalCancelado=totalCancelado+it.total
	    				}*/
						if(cabecerasMostradas.indexOf(it.ordenReserva)<0){
							cabecerasMostradas.add(it.ordenReserva)
							mostrarTotal=true
						}else{
							mostrarTotal=false
						}
						
	    				saldo=it.ordenReserva.total-it.ordenReserva.recibo-it.ordenReserva.credito+it.ordenReserva.debito
						
						sheet.addCell(new Label(0,fil,it.ordenReserva.usuario.userRealName))
						sheet.addCell(new Label(1,fil,it.ordenReserva.vendedor.nombre))
						
						
    					sheet.addCell(new Label(2,fil,it.ordenReserva.nombre))
    					sheet.addCell(new Label(3,fil,it.sector?.nombre))    					
    					sheet.addCell(new Label(4,fil,it.lote?.nombre)) 
						sheet.addCell(new Number(5,fil,(mostrarTotal?it.ordenReserva.subtotalDetalle:0) ))
						sheet.addCell(new Number(6,fil,(mostrarTotal?it.ordenReserva.subtotalOtrosConceptos:0)))
    					sheet.addCell(new Number(7,fil,(mostrarTotal?it.ordenReserva.total:0)))
    					sheet.addCell(new Number(8,fil,(mostrarTotal?it.ordenReserva.debito:0)))
						sheet.addCell(new Number(9,fil,(mostrarTotal?it.ordenReserva.credito:0)))
						sheet.addCell(new Number(10,fil,(mostrarTotal?it.ordenReserva.recibo:0)))
    					sheet.addCell(new Number(11,fil,(mostrarTotal?saldo:0)))
    					sheet.addCell(new Label(12,fil,it.ordenReserva.expo.nombre))
    					sheet.addCell(new Number(13,fil,it.ordenReserva.anio))
    					sheet.addCell(new Number(14,fil,it.ordenReserva.numero))
    					sheet.addCell(new DateTime(15,fil,it.ordenReserva.fechaAlta,dateFormat))
						sheet.addCell(new Label(16,fil,it.ordenReserva.razonSocial))
						sheet.addCell(new Label(17,fil,it.ordenReserva.cuit))
						sheet.addCell(new Label(18,fil,it.ordenReserva.direccion))
						sheet.addCell(new Label(19,fil,it.ordenReserva.email))
						sheet.addCell(new Label(20,fil,it.ordenReserva.nombreRepresentante))
						sheet.addCell(new Label(21,fil,it.ordenReserva.dniRep))
						sheet.addCell(new Label(22,fil,it.ordenReserva.telefonoRepresentante1))
						sheet.addCell(new Label(23,fil,it.ordenReserva.telefonoRepresentante2))
						sheet.addCell(new Label(24,fil,it.ordenReserva.telefonoRepresentante3))
						sheet.addCell(new Label(25,fil,it.ordenReserva.empresa.sitioWeb))
						sheet.addCell(new Label(26,fil,it.ordenReserva.empresa.subrubro?.rubro?.nombreRubro))
						sheet.addCell(new Label(27,fil,it.ordenReserva.empresa.subrubro?.nombreSubrubro))
						sheet.addCell(new Label(28,fil,it.ordenReserva.empresa.vendedor.nombre))
						sheet.addCell(new Label(29,fil,it.ordenReserva.empresa.localidad?.provincia?.nombre))
						sheet.addCell(new Label(30,fil,it.ordenReserva.empresa.localidad?.nombreLoc))
						sheet.addCell(new Label(31,fil,it.ordenReserva.observacion))
						
    					fil=fil+1    					    					    					    					    					    					    					
    				}else{
    					/*it.recibos.each{r->
    						if(!r.anulado)
    							totalCancelado=totalCancelado+r.total
    					}*/
						if(cabecerasMostradas.indexOf(it)<0){
							cabecerasMostradas.add(it)
							mostrarTotal=true
						}else{
							mostrarTotal=false
						}
					
    					saldo=it.total-it.recibo-it.credito+it.debito
						
						sheet.addCell(new Label(0,fil,it.usuario.userRealName))
						sheet.addCell(new Label(1,fil,it.vendedor.nombre))
						
    					sheet.addCell(new Label(2,fil,it.nombre))
    					sheet.addCell(new Label(3,fil,""))    					
    					sheet.addCell(new Label(4,fil,""))
						sheet.addCell(new Number(5,fil,(mostrarTotal?it.subtotalDetalle:0)))
						sheet.addCell(new Number(6,fil,(mostrarTotal?it.subtotalOtrosConceptos:0)))
    					sheet.addCell(new Number(7,fil,(mostrarTotal?it.total:0)))
    					sheet.addCell(new Number(8,fil,(mostrarTotal?it.debito:0))) 
						sheet.addCell(new Number(9,fil,(mostrarTotal?it.credito:0)))
						sheet.addCell(new Number(10,fil,(mostrarTotal?it.recibo:0)))
    					sheet.addCell(new Number(11,fil,(mostrarTotal?saldo:0)))
    					sheet.addCell(new Label(12,fil,it.expo.nombre))
    					sheet.addCell(new Number(13,fil,it.anio))
    					sheet.addCell(new Number(14,fil,it.numero))
    					sheet.addCell(new DateTime (15,fil,it.fechaAlta,dateFormat))
						sheet.addCell(new Label(16,fil,it.razonSocial))
						sheet.addCell(new Label(17,fil,it.cuit))
						sheet.addCell(new Label(18,fil,it.direccion))
						sheet.addCell(new Label(19,fil,it.email))
						sheet.addCell(new Label(20,fil,it.nombreRepresentante))
						sheet.addCell(new Label(21,fil,it.dniRep))
						sheet.addCell(new Label(22,fil,it.telefonoRepresentante1))
						sheet.addCell(new Label(23,fil,it.telefonoRepresentante2))
						sheet.addCell(new Label(24,fil,it.telefonoRepresentante3))
						sheet.addCell(new Label(25,fil,it.empresa.sitioWeb))
						sheet.addCell(new Label(26,fil,it.empresa.subrubro?.rubro?.nombreRubro))
						sheet.addCell(new Label(27,fil,it.empresa.subrubro?.nombreSubrubro))
						sheet.addCell(new Label(28,fil,it.empresa.vendedor.nombre))
						sheet.addCell(new Label(29,fil,it.empresa.localidad?.provincia?.nombre))
						sheet.addCell(new Label(30,fil,it.empresa.localidad?.nombreLoc))
						sheet.addCell(new Label(31,fil,it.observacion))

    					fil=fil+1    					    					    					    					    					    					    					
    				}
    				
    	}
      	workbook.write()  
	    workbook.close()  	
    }
    
}
