//import com.lowagie.text.pdf.AcroFields.Item;

package com.rural
import com.rural.seguridad.Person
import com.rural.enums.TipoNotaEnum
import com.rural.enums.TipoGeneracionEnum

class ReciboException extends RuntimeException{
	String message
	Recibo recibo
	
	public ReciboException(String message, Recibo rec){
		super(message)
		this.message=message
		this.recibo=rec
	}
}


class ReciboService {

    boolean transactional = true

    Recibo generarRecibo(Long ordId,String concepto,Double efectivo,cheques,Person user) {
    		def ord = OrdenReserva.get(ordId)
    		
    		Double totalCancelado = 0
    		ord.recibos.each{
    			if(it.anulado==false)
    				totalCancelado = totalCancelado + it.total
    		}
    		
    		if(ord){
    			def recibo = new Recibo(fechaAlta:new Date(),ordenReserva:ord,efectivo:efectivo,concepto:concepto,total:0,usuario:user)
    			
    			cheques.each{
    				recibo.addToCheques(it)
    				recibo.total=recibo.total+it.importe	
    			}
    			recibo.total=recibo.total+efectivo
    			recibo.total=Math.round(recibo.total*Math.pow(10,2))/Math.pow(10,2);
    			if ((recibo.total+totalCancelado)>ord.total)
    				throw new ReciboException('El total ('+recibo.total+') de recibo supera el total ('+ord.total+') pendiente de pago de la orden de reserva',recibo)
    			if(recibo.save()){
					verificarVencimiento(ord,recibo)
    				return recibo
    			}else{
    				throw new ReciboException('El recibo no se pudo crear por un error de validacion',recibo)
    			}
    		}else{
    			throw new ReciboException('No se puede crear un recibo con una orden de reserva inexistente',recibo)
    		}
    }
    
    boolean anularRecibo(Long id){ 
    	def reciboInstance = Recibo.get(id)
    	if (reciboInstance){
    		reciboInstance.anulado = true
    		reciboInstance.save()
    	}else
    		throw new ReciboException("No se pudo anular el recibo. Recibo inexistente, $id",reciboInstance)
    	
    }    
	
	private void verificarVencimiento(def orden,def recibo){
		def notad
		def notadDetalle 
		def today = new Date()
		def todaysql = new java.sql.Date(today.getTime())
		boolean debitoCreado = false
		
		//= new NotaDC(ordenReserva:orden,tipo:TipoNotaEnum.NOTA_DEBITO)
		log.info "INGRESANDO AL METODO PRIVADO verificarVencimiento"
		if(orden.fechaVencimiento){
			orden.notas.each{
				if(it.tipo==TipoNotaEnum.NOTA_DEBITO && it.tipoGen== TipoGeneracionEnum.TIPOGEN_AUTOMATICA )
					debitoCreado=true
			}
			if(orden.fechaVencimiento<todaysql && orden.detalle.size() && debitoCreado==false){
					notad = new NotaDC(fechaAlta:todaysql, ordenReserva:orden,tipoGen:TipoGeneracionEnum.TIPOGEN_AUTOMATICA,tipo:TipoNotaEnum.NOTA_DEBITO,monto:"0".toDouble()
						,subTotal:"0".toDouble(),ivaGral:"0".toDouble(),ivaRni:"0".toDouble(),ivaSujNoCateg:"0".toDouble())
					orden.detalle.each {
						if(it.subTotalsindesc-it.subTotal >0){
							 notadDetalle = new NotadcDetalle(descripcion:"Quita de Descuento del ${it.sector.porcentaje} por Sector ${it.sector.nombre}",subTotal:it.subTotalsindesc-it.subTotal)
							 notad.addToDetalle(notadDetalle)
							 notad.subTotal = notad.subTotal + notadDetalle.subTotal
						}
					}
					notad.ivaGral =  notad.subTotal*(orden.porcentajeResIns > 0 ? orden.porcentajeResIns : orden.porcentajeResNoIns)/100
					if(orden.ivaRniCheck)
						notad.ivaSujNoCateg=notad.ivaRni*10.5/100
					notad.total=notad.subTotal+notad.ivaGral+notad.ivaSujNoCateg
					notad.total=Math.round(notad.total*Math.pow(10,2))/Math.pow(10,2)
					if(notad.subTotal>0){
						if(notad.save()){
							orden.addToNotas(notad)
							if(orden.save())
								log.info "SE GENERO UNA NOTA DE DEBITO de ${notad.total} PARA LA ORDEN CON ID: ${orden.id}"
							else
								throw new ReciboException("Se produjo un error al agregar una nota como detalle de la orden de reserva",recibo)	
						}else{
							log.info "ERRORES DE RECIBO: "+notad.errors.allErrors
							throw new ReciboException("Se produjo un error al generar una nota de débito, operación cancelada",recibo)
						}
					}
			}
		}
		log.info "VERIFICACION CONCLUIDA CORRECTAMENTE"
	}
    
}
