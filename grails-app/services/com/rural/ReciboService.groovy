package com.rural

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

    Recibo generarRecibo(Long ordId) {
    		def ord = OrdenReserva.get(ordId)
    		
    		if(ord){
    			def recibo = new Recibo(fechaAlta:new Date(),ordenReserva:ord,total:ord.total)
    			
    			if(recibo.save()){
    				return recibo
    			}else{
    				throw new ReciboException('El recibo no se pudo crear por un error de validacion',recibo)
    			}
    		}else{
    			throw new ReciboException('No se puede crear un recibo con una orden de reserva inexistente',recibo)
    		}
    }
}
