package com.rural
// http://www.grails.org/doc/1.0.x/guide/17.%20Deployment.html
import java.util.Locale
import java.sql.Date
import java.util.Calendar
import groovy.sql.Sql
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import java.text.SimpleDateFormat
import com.rural.enums.TipoNotaEnum
import com.rural.enums.TipoGeneracionEnum
import java.util.Collection



class OrdenReservaException extends RuntimeException{
	
	String message
	OrdenReserva ordenReserva
	
	public OrdenReservaException(String message,OrdenReserva ord){
		super(message)
		this.message=message
		this.ordenReserva=ord
	}
}

class OrdenReservaService {
	boolean transactional = true
	
	def messageSource
	
	void asignarDescuentosSubtotal(def detalle){
		log.info "INGRESANDO AL METODO asignarDescuentosSubtotal DEL SERVICIO OrdenReservaService"
		def difSubTotal
		def difDesc
		def primerDesc=0
		def primeraFechaVencimiento
		def todayCal = Calendar.getInstance()
		def sf = new SimpleDateFormat("yyyy-MM-dd")
		def fecha =  java.sql.Date.valueOf(sf.format(todayCal.getTime()))

		def listDescuentos = ListaDescuentos.createCriteria().list(){
			and{
				sector{
					eq("id",detalle.sector.id)
				}
				ge("fechaVencimiento",fecha)
			}
			order("fechaVencimiento","asc")
			
		}
		primerDesc=listDescuentos[0]?.porcentaje
		primeraFechaVencimiento=listDescuentos[0]?.fechaVencimiento
		if(primerDesc==null)
			primerDesc=0
		if(detalle.lote?.precio){
			log.debug "PRECIO DE LOTE APLICADO: ${detalle.lote.precio}"
			detalle.subTotalsindesc = detalle.lote.precio
			detalle.subTotal = detalle.lote.precio - detalle.lote.precio*primerDesc/100
		}else{
			log.debug "PRECIO DE SECTOR APLICADO: ${detalle.sector.precio}"
			if(detalle.sector?.precio){
				detalle.subTotalsindesc = detalle.sector.precio
				detalle.subTotal = detalle.sector.precio - detalle.sector.precio*primerDesc/100
			}else{
				detalle.subTotalsindesc = detalle.subTotal
			}
		
		}

	
		
		listDescuentos.eachWithPeek{current,peek->
			
			if(current.fechaVencimiento.compareTo(fecha)>=0){
				log.debug "DESCUENTO ITERADO: actual: ${current.fechaVencimiento} porcentaje: ${current.porcentaje}, siguiente: ${peek?.fechaVencimiento} porcentaje: ${peek?.porcentaje}"
				
				difDesc = current.porcentaje - (peek?.porcentaje==null?0:peek.porcentaje)
				difSubTotal = detalle.subTotal*difDesc/100
				log.debug "Diferencia de descuento: ${difDesc}, subTotal diferencia: ${difSubTotal}, subtotal de detalle:${detalle.subTotal}"
				detalle.addToDescuentos(new DetalleServicioContratadoDescuentos(porcentaje:difDesc
						,fechaVencimiento:current.fechaVencimiento,subTotal:difSubTotal,porcentajeActual:current?.porcentaje,porcentajeSig:peek?.porcentaje))
			}
		}
				
		detalle.subTotal = detalle.subTotalsindesc - detalle.subTotalsindesc * primerDesc/100
		detalle.porcentajeDesc = primerDesc
		detalle.fechaVencimiento = primeraFechaVencimiento
		log.info "CANTIDAD DE DESCUENTOS AGREGADOS AL DETALLE DEL SERVICIO CONTRATADO: "+detalle.descuentos?.size()
		log.info "DESCUENTO APLICADO: ${primerDesc}"
		log.info "SUBTOTAL DEL DETALLE: ${detalle.subTotal}"
		log.info "SUBTOTAL SIN DESCUENTO DEL DETALLE: ${detalle.subTotalsindesc}"
	}
	
	
	def vincularDescuentosConDetalle(def detalle){
		log.info "INGRESANDO AL METODO vincularDescuentosConDetalle DEL SERVICIO OrdenReservaService"
		def difSubTotal
		def difDesc
		def primerDesc=0
		def primeraFechaVencimiento
		def todayCal = Calendar.getInstance()
		def sf = new SimpleDateFormat("yyyy-MM-dd")
		def fecha =  java.sql.Date.valueOf(sf.format(todayCal.getTime()))

		def listDescuentos = ListaDescuentos.createCriteria().list(){
			and{
				sector{
					eq("id",detalle.sector.id)
				}
				ge("fechaVencimiento",fecha)
			}
			order("fechaVencimiento","asc")
			
		}
		primerDesc=listDescuentos[0]?.porcentaje
		primeraFechaVencimiento=listDescuentos[0]?.fechaVencimiento
		if(primerDesc==null)
			primerDesc=0
		if(detalle.lote?.precio){
			log.debug "PRECIO DE LOTE APLICADO: ${detalle.lote.precio}"
			detalle.subTotalsindesc = detalle.lote.precio
			detalle.subTotal = detalle.lote.precio //- detalle.lote.precio*primerDesc/100
		}else{
			log.debug "PRECIO DE SECTOR APLICADO: ${detalle.sector.precio}"
			if(detalle.sector?.precio){
				detalle.subTotalsindesc = detalle.sector.precio
				detalle.subTotal = detalle.sector.precio //- detalle.sector.precio*primerDesc/100
			}else{
				detalle.subTotalsindesc = detalle.subTotal
			}
		
		}

	
		def subTotalDesc=0
		listDescuentos.eachWithPeek{current,peek->
			
			//if(current.fechaVencimiento.compareTo(fecha)>=0){
			//	log.debug "DESCUENTO ITERADO: actual: ${current.fechaVencimiento} porcentaje: ${current.porcentaje}, siguiente: ${peek?.fechaVencimiento} porcentaje: ${peek?.porcentaje}"
				subTotalDesc = detalle.subTotal - detalle.subTotal * current?.porcentaje/100
				detalle.addToDescuentos(new DetalleServicioContratadoDescuentos(porcentaje:current.porcentaje
						,fechaVencimiento:current.fechaVencimiento,subTotal:subTotalDesc,porcentajeActual:current?.porcentaje,porcentajeSig:peek?.porcentaje))
			//}
		}
				

	}
	

    OrdenReserva generarOrdenReserva(OrdenReserva ord,Empresa empresa) {
		log.info "INGRESANDO AL METODO generaOrdenReserva DE OrdenReservaService"
		log.info "PARAMETROS: $ord, $empresa"
		log.debug "CANTIDAD DETALLES SERVICIO CONTRATADO: ${ord.detalle?.size()}"
		def precio=0
		
		Collection.metaClass.eachWithPeek = { closure ->
			def last = null
			delegate?.each { current ->
				if (last) closure(last, current)
				last = current
			}
			if (last) closure(last, null)
		}

		
    	ord.otrosconceptos.each{
    		ord.subTotal+=it.subTotal
			ord.subTotalsindesc+=it.subTotal
    	}
    	if (!empresa.validate()){
    		throw new OrdenReservaException("Ocurrio un error al tratar de salvar los datos de la empresa. "
    			+empresa.errors.allErrors,ord)
   		}
    	def empresaInstance = empresa.save()
    	
    	if(!empresaInstance)
    		throw new OrdenReservaException("Ocurrio un error. No se guardo la empresa")
			
    	ord.detalle?.eachWithPeek{current, peek->
			log.debug "CURRENT: ${current?.class}, Next: ${peek?.class}"
		}
	    ord.detalle?.each{det->
				
				/*precio=det.lote.precio
				if(precio==0)
					precio=det.sector.precio
				det.subTotal = precio - precio * det.sector?.porcentaje/100
				det.subTotalsindesc = precio
				ord.subTotal = ord.subTotal + det.subTotal
				ord.subTotalsindesc=ord.subTotalsindesc+det.subTotalsindesc*/
				vincularDescuentosConDetalle(det)
				ord.subTotal = ord.subTotal + det.subTotal
				ord.subTotalsindesc=ord.subTotalsindesc+det.subTotalsindesc

    		}
    	ord.ivaGral = ord.ivaGralCalc //ord.ivaGral = ord.subTotal*(ord.porcentajeResIns > 0 ? ord.porcentajeResIns : ord.porcentajeResNoIns)/100
		log.debug "IVA GRAL: "+ord.ivaGral
    	ord.ivaRni = ord.ivaRniCalc//ord.ivaRni=ord.subTotal+ord.ivaGral
    	//if(ord.ivaRniCheck && ord.porcentajeResIns>=0)//modificacion clave para determinar el porcentaje de iva cuando la expo es exenta
    	ord.ivaSujNoCateg = ord.ivaSujNoCategCalc//	ord.ivaSujNoCateg=ord.ivaRni*10.5/100
		log.debug "TOTAL GRAL FORMADO POR subTotal: "+ord.subTotal+", ivaGral: "+ord.ivaGral+" ivaSujNoCateg: "+ord.ivaSujNoCateg
		log.debug "TOTAL GRAL SIN DESCUENTOS FORMADO POR subTotalsindesc: "+ord.subTotalsindesc+", ivaGral: "+ord.ivaGral+" ivaSujNoCateg: "+ord.ivaSujNoCateg	
    	ord.total=ord.subTotal+ord.ivaGral+ord.ivaSujNoCateg
		
		def ivaGralSindesc = ord.ivaGralCalc //= ord.subTotalsindesc*(ord.porcentajeResIns > 0 ? ord.porcentajeResIns : ord.porcentajeResNoIns)/100

        def ivaSujNoCategSindesc = ord.ivaSujNoCategCalc//def ivaSujNoCategSindesc= 0
		//if(ord.ivaRniCheck && ord.porcentajeResIns>=0)//modificacion clave para determinar el porcentaje de iva cuando la expo es exenta
		//	ivaSujNoCategSindesc = ord.ivaRni*10.5/100 
		ord.totalsindesc = ord.subTotalsindesc+ivaGralSindesc+ivaSujNoCategSindesc
    	ord.total=Math.round(ord.total*Math.pow(10,2))/Math.pow(10,2);
		ord.totalsindesc = Math.round(ord.totalsindesc*Math.pow(10,2))/Math.pow(10,2);
		ord.empresa=empresaInstance
		ord.fechaAlta=new java.sql.Date((new java.util.Date()).getTime())
    	if(ord.validate()){
    		ord = ord.save() 
			log.debug "TOTAL DE ORDEN DE RESERVA: "+ord.total
    		return ord
    		
    	}else{
    		log.debug "ORDEN CON ERRORES "+ord.errors.allErrors
			
    		def message=''
    		/*ord.errors.allErrors.each{
    			message+=it.toString()
    		}*/
    		log.debug("Errores encontrados: "+ord.errors.allErrors)
    		/*ord.errors.allErrors.each{error->
    			error.codes.each{
    				if(messageSource.getMessage(it,new Locale("es"))!=it)
    					//errorList.add(g.message(code:it))
    					message+=messageSource.getMessage(it,new Locale("es"))		
    			}
    		}*/
    		throw new OrdenReservaException(message,ord);
    	}
    		
    }
    
	void actualizarSaldos(){
		log.info "INGRESANDO AL METODO actualizarSaldos DEL Service OrdenReservaService"
		def sql = Sql.newInstance(ConfigurationHolder.config.dataSource.url,
			ConfigurationHolder.config.dataSource.username,
			ConfigurationHolder.config.dataSource.password,
			ConfigurationHolder.config.dataSource.driverClassName)
		def today = new Date()
		def todaysql = new java.sql.Date(today.getTime())
		def formatter = new SimpleDateFormat("yyyy-MM-dd")
		dateFormated = formatter.format(todaysql)
		sql.eachRow("""SELECT * from orden_reserva where anulada = 1 and fecha_Vencimiento<='${dateFormated}' """) { o ->
			log.debug ""
					
			
		 }
	}
	
    boolean anularOrdenReserva(Long id){
    	def ordenReservaInstance = OrdenReserva.get(id)
    	if (ordenReservaInstance){
			if(ordenReservaInstance.recibo>0){
				throw new OrdenReservaException("No se puede anular la orden de reserva porque tiene recibos asociados",ordenReservaInstance)
			}
			
    		ordenReservaInstance.anulada = true
			ordenReservaInstance.notas.each { 
				it.anulada = true	
			}
			
    		ordenReservaInstance.save()
    	}else
    		throw new OrdenReservaException("No se pudo anular la orden de reserva. Orden inexistente, $id",ordenReservaInstance)
    	
    }
	
	
	void verificarVencimiento(def detalleServContDesc){
		log.info "INGRESANDO AL CLOSURE verificarVencimiento DEL SERVICE OrdenReservaService"
		log.info "Detalle Servicio Contratado Descuento: $detalleServContDesc"
		
		def notad
		def notadDetalle
		def today = new java.util.Date()
		def sdf = new SimpleDateFormat("yyyy-MM-dd")
		log.info "OPERACION CON FECHA: "+java.sql.Date.valueOf(sdf.format(today))
		//def todaysql = java.sql.Date.valueOf(sdf.format(today)) //new java.sql.Date(today.getTime())
		boolean debitoCreado = false
		def orden=detalleServContDesc.detalleServicioContratado.ordenReserva
		notad = new NotaDC(fechaAlta:java.sql.Date.valueOf(sdf.format(today))
			, ordenReserva:orden,tipoGen:TipoGeneracionEnum.TIPOGEN_AUTOMATICA
			,tipo:TipoNotaEnum.NOTA_DEBITO
			,monto:"0".toDouble()
			,subTotal:"0".toDouble(),ivaGral:"0".toDouble(),ivaRni:"0".toDouble(),ivaSujNoCateg:"0".toDouble())
		notadDetalle = new NotadcDetalle(descripcion:"Quita de Descuento del ${detalleServContDesc.porcentaje}% (${detalleServContDesc.porcentajeActual}% menos ${detalleServContDesc.porcentajeSig}%) por Sector ${detalleServContDesc.detalleServicioContratado.sector.nombre}"
					,subTotal:detalleServContDesc.subTotal)
		notad.addToDetalle(notadDetalle)
		notad.subTotal = notadDetalle.subTotal
		notad.ivaGral =  notad.subTotal*(orden.porcentajeResIns > 0 ? orden.porcentajeResIns : orden.porcentajeResNoIns)/100
		notad.ivaRni = notad.subTotal+notad.ivaGral 
		log.debug "CALCULO DE IVA GRAL: "+notad.ivaGral
		if(orden.ivaRniCheck && orden.porcentajeResIns>=0)
			notad.ivaSujNoCateg=notad.ivaRni*10.5/100
		notad.total=notad.subTotal+notad.ivaGral+notad.ivaSujNoCateg
		notad.total=Math.round(notad.total*Math.pow(10,2))/Math.pow(10,2)
		if(notad.subTotal>0){
			log.info "SUBTOTAL MAYOR A CERO"
			if(notad.save()){
				log.info "NOTA DEBITO SALVADA"
				orden.addToNotas(notad)
				detalleServContDesc.notadcDetalle=notadDetalle
				if(orden.save()){
					
					log.info "SE GENERO UNA NOTA DE DEBITO de ${notad.total} PARA LA ORDEN CON ID: ${orden.id}"
				}else
					log.info "Se produjo un error al agregar una nota como detalle de la orden de reserva"
				
			}else{
				log.info "ERRORES DE NOTA DE DEBITO: "+notad.errors.allErrors
				
			}
		}else
			log.error "SUBTOTAL DE NOTA DE DEBITO IGUAL A CERO"

		/*
		log.debug "ANTES DEL IF orden.fechaVencimiento"
		if(orden.fechaVencimiento){
			orden.notas.each{
				if(it.tipo==TipoNotaEnum.NOTA_DEBITO && it.tipoGen== TipoGeneracionEnum.TIPOGEN_AUTOMATICA )
					debitoCreado=true
			}
			log.debug "ANTES DEL IF QUE COMPARA LA FECHA DE VENCIMIENTO CON LA FECHA DE HOY Y EL TAMA�O DEL DETALLE"
			if(orden.fechaVencimiento<java.sql.Date.valueOf(sdf.format(today)) && orden.detalle.size() && debitoCreado==false){
					log.debug "DENTRO DEL IF QUE COMPARA LA FECHA DE VENCIMIENTO CON EL DIA DE HOY"
					notad = new NotaDC(fechaAlta:java.sql.Date.valueOf(sdf.format(today)), ordenReserva:orden,tipoGen:TipoGeneracionEnum.TIPOGEN_AUTOMATICA,tipo:TipoNotaEnum.NOTA_DEBITO,monto:"0".toDouble()
						,subTotal:"0".toDouble(),ivaGral:"0".toDouble(),ivaRni:"0".toDouble(),ivaSujNoCateg:"0".toDouble())
					orden.detalle.each {
						log.debug "DENTRO DEL EACH DEL DETALLE"
						if(it.subTotalsindesc-it.subTotal >0){
							 notadDetalle = new NotadcDetalle(descripcion:"Quita de Descuento del ${it.sector.porcentaje} por Sector ${it.sector.nombre}",subTotal:it.subTotalsindesc-it.subTotal)
							 notad.addToDetalle(notadDetalle)
							 notad.subTotal = notad.subTotal + notadDetalle.subTotal
						}
					}
					log.debug "ANTES DEL CALCULO DE IVA GRAL"
					notad.ivaGral =  notad.subTotal*(orden.porcentajeResIns > 0 ? orden.porcentajeResIns : orden.porcentajeResNoIns)/100
					log.debug "CALCULO DE IVA GRAL: "+notad.ivaGral
					if(orden.ivaRniCheck)
						notad.ivaSujNoCateg=notad.ivaRni*10.5/100
					notad.total=notad.subTotal+notad.ivaGral+notad.ivaSujNoCateg
					notad.total=Math.round(notad.total*Math.pow(10,2))/Math.pow(10,2)
					if(notad.subTotal>0){
						log.info "SUBTOTAL MAYOR A CERO"
						if(notad.save()){
							log.info "NOTA DEBITO SALVADA" 
							orden.addToNotas(notad)
							if(orden.save())
								log.info "SE GENERO UNA NOTA DE DEBITO de ${notad.total} PARA LA ORDEN CON ID: ${orden.id}"
							else
								log.info "Se produjo un error al agregar una nota como detalle de la orden de reserva"
							
						}else{
							log.info "ERRORES DE NOTA DE DEBITO: "+notad.errors.allErrors
							
						}
					}
			}
		}else{
			log.info "FECHA DE VENCIMIENTO NO VALIDA"
		}*/
		
	}

	def generarnota(Long ordenId,NotaDC nota){
		log.info "INGRESANDO AL PROCEDIMIENTO generarnota DEL SERVICIO OrdenReservaService"
		log.info "PARAMETROS, orden: $ordenId, nota con subtotal: ${nota.subTotal}"
		
		def ordenReservaInstance = OrdenReserva.get(ordenId)
		if(!ordenReservaInstance)
			throw new OrdenReservaException("com.rural.noexiste",ordenReservaInstance)

		nota.ivaGral = nota.subTotal*(ordenReservaInstance.porcentajeResIns > 0 ? ordenReservaInstance.porcentajeResIns : ordenReservaInstance.porcentajeResNoIns)/100
		nota.ivaRni = nota.subTotal+nota.ivaGral
		if(ordenReservaInstance.ivaRniCheck && ordenReservaInstance.porcentajeResIns>=0)
			nota.ivaSujNoCateg=nota.ivaRni*10.5/100
		log.info "TOTAL GRAL FORMADO POR subTotal: "+nota.subTotal+", ivaGral: "+nota.ivaGral+" ivaSujNoCateg: "+nota.ivaSujNoCateg
		
		nota.total=nota.subTotal+nota.ivaGral+nota.ivaSujNoCateg
	
						
		//ordenReservaInstance.addToNotas(nota)
		nota.ordenReserva = ordenReservaInstance
		if(nota.validate() && nota.save())
			return nota.id
		else
			return nota	
	}
	

	
}

