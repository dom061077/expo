package com.rural
// http://www.grails.org/doc/1.0.x/guide/17.%20Deployment.html
import java.util.Locale
import java.sql.Date
import groovy.sql.Sql
import org.codehaus.groovy.grails.commons.ConfigurationHolder


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
    	
    	//if(!empresaInstance.subrubro.save())
    	//	throw new OrdenReservaException("Error al guardar el subrubro",ord)
    	log.debug "SUBTOTAL DE ORDE DE RESERVA ANTES DE ITERAR EL DETALLE: "+ord.subTotal+" Y SUBTOTALSININTERES: "+ord.subTotalsindesc
	    ord.detalle.each{
				log.debug "DETALLE SERVICIO CONTRATADO "
				log.debug "PORCENTAJE DE DESCUENTO DEL SECTOR: "+it.sector.porcentaje
				log.debug "PRECIO DEL LOTE: "+it.lote.nombre+" "+it.lote.precio
				log.debug "TIENE VENCIMIENTO"
				it.subTotal = it.lote.precio - it.lote.precio * it.sector?.porcentaje/100
				it.subTotalsindesc=it.lote.precio
				ord.subTotal = ord.subTotal + it.subTotal
				ord.subTotalsindesc=ord.subTotalsindesc+it.subTotalsindesc
    		}
    	log.debug "DESPUES DE ITERAR EL DETALLE EL SUBTOTAL ES:"+ord.subTotal	
    	ord.ivaGral = ord.subTotal*(ord.porcentajeResIns > 0 ? ord.porcentajeResIns : ord.porcentajeResNoIns)/100
		log.debug "IVA GRAL: "+ord.ivaGral
    	//ord.ivaRni = ord.subTotal*ord.porcentajeResNoIns/100	
    	ord.ivaRni=ord.subTotal+ord.ivaGral
    	if(ord.ivaRniCheck)
    		ord.ivaSujNoCateg=ord.ivaRni*10.5/100
		log.debug "TOTAL GRAL FORMADO POR subTotal: "+ord.subTotal+", ivaGral: "+ord.ivaGral+" ivaSujNoCateg: "+ord.ivaSujNoCateg
		log.debug "TOTAL GRAL SIN DESCUENTOS FORMADO POR subTotalsindesc: "+ord.subTotalsindesc+", ivaGral: "+ord.ivaGral+" ivaSujNoCateg: "+ord.ivaSujNoCateg	
    	ord.total=ord.subTotal+ord.ivaGral+ord.ivaSujNoCateg
		def ivaGralSindesc = ord.subTotalsindesc*21/100
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

		sql.eachRow("""SELECT * from orden_reserva where anulada = 1 and """) { o ->
			
					
			
		 }
	}
	
    boolean anularOrdenReserva(Long id){
    	def ordenReservaInstance = OrdenReserva.get(id)
    	if (ordenReservaInstance){
    		ordenReservaInstance.anulada = true
    		ordenReservaInstance.save()
    	}else
    		throw new OrdenReservaException("No se pudo anular la orden de reserva. Orden inexistente, $id",ordenReservaInstance)
    	
    }
}

