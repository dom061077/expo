package com.rural
import com.rural.seguridad.Person

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
    
}
