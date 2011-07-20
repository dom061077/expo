package com.rural
// http://www.grails.org/doc/1.0.x/guide/17.%20Deployment.html
import java.util.Locale
import java.sql.Date
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
			
    	ord.detalle.eachWithPeek{current, peek->
			log.debug "CURRENT: ${current?.class}, Next: ${peek?.class}"
		}
	    ord.detalle.each{det->
				precio=det.lote.precio
				if(precio==0)
					precio=det.sector.precio
				det.subTotal = precio - precio * det.sector?.porcentaje/100
				det.subTotalsindesc = precio
				ord.subTotal = ord.subTotal + det.subTotal
				ord.subTotalsindesc=ord.subTotalsindesc+det.subTotalsindesc
				it.sector.descuentos.eachWithPeek{current,peek->
					desc.addToDescuentos(new DetalleServicioContratadoDescuentos(porcentaje:desc.porcentaje,fechaVencimiento:desc.fechaVencimiento,subTotal:desc.subTotal))
				}
    		}
    	ord.ivaGral = ord.subTotal*(ord.porcentajeResIns > 0 ? ord.porcentajeResIns : ord.porcentajeResNoIns)/100
		log.debug "IVA GRAL: "+ord.ivaGral
    	ord.ivaRni=ord.subTotal+ord.ivaGral
    	if(ord.ivaRniCheck)
    		ord.ivaSujNoCateg=ord.ivaRni*10.5/100
		log.debug "TOTAL GRAL FORMADO POR subTotal: "+ord.subTotal+", ivaGral: "+ord.ivaGral+" ivaSujNoCateg: "+ord.ivaSujNoCateg
		log.debug "TOTAL GRAL SIN DESCUENTOS FORMADO POR subTotalsindesc: "+ord.subTotalsindesc+", ivaGral: "+ord.ivaGral+" ivaSujNoCateg: "+ord.ivaSujNoCateg	
    	ord.total=ord.subTotal+ord.ivaGral+ord.ivaSujNoCateg
		
		def ivaGralSindesc = ord.subTotalsindesc*(ord.porcentajeResIns > 0 ? ord.porcentajeResIns : ord.porcentajeResNoIns)/100
		
		def ivaSujNoCategSindesc= 0
		if(ord.ivaRniCheck)
			ivaSujNoCategSindesc = ord.ivaRni*10.5/100 
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
    		ordenReservaInstance.anulada = true
			ordenReservaInstance.notas.each { 
				it.anulada = true	
			}
			
    		ordenReservaInstance.save()
    	}else
    		throw new OrdenReservaException("No se pudo anular la orden de reserva. Orden inexistente, $id",ordenReservaInstance)
    	
    }
	
	
	void verificarVencimiento(def orden){
		log.info "INGRESANDO AL CLOSURE verificarVencimiento DEL SERVICE OrdenReservaService"
		log.info "ORDEN: $orden"
		
		def notad
		def notadDetalle
		def today = new java.util.Date()
		def sdf = new SimpleDateFormat("yyyy-MM-dd")
		log.info "OPERACION CON FECHA: "+java.sql.Date.valueOf(sdf.format(today))
		//def todaysql = java.sql.Date.valueOf(sdf.format(today)) //new java.sql.Date(today.getTime())
		boolean debitoCreado = false
		
		log.debug "ANTES DEL IF orden.fechaVencimiento"
		if(orden.fechaVencimiento){
			orden.notas.each{
				if(it.tipo==TipoNotaEnum.NOTA_DEBITO && it.tipoGen== TipoGeneracionEnum.TIPOGEN_AUTOMATICA )
					debitoCreado=true
			}
			log.debug "ANTES DEL IF QUE COMPARA LA FECHA DE VENCIMIENTO CON LA FECHA DE HOY Y EL TAMAÑO DEL DETALLE"
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
		}
		
	}

	def generarnota(Long ordenId,NotaDC nota){
		log.info "INGRESANDO AL PROCEDIMIENTO generarnota DEL SERVICIO OrdenReservaService"
		log.info "PARAMETROS, orden: $ordenId, tipo: $tipo"
		
		def ordenReservaInstance = OrdenReserva.get(ordenId)
		if(!ordenReservaInstance)
			return null
		ordenReservaInstance.addToNotas(nota)
		if(!ordenReservaInstance.validate() && ordenReservaInstance.save())
			return nota.id
		else
			return ordenReservaInstance	
	}
	
	void tirarmensaje(){
		log.info "MENSAJE DESDE METODO"
	}

	
}

