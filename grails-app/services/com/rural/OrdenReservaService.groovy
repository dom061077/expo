package com.rural

class OrdenReservaException extends RuntimeException{
	String message
	OrdenReserva ordenReserva
	boolean transactional = true 
	
	public OrdenReservaException(String message,OrdenReserva ord){
		super(message)
		this.message=message
		this.ordenReserva=ord
	}
}

class OrdenReservaService {

    

    OrdenReserva generarOrdenReserva(OrdenReserva ord,Empresa empresa) {
    	def empresaInstance = empresa.save()
    	
    	if (empresaInstance==null)
    		throw new OrdenReservaException(message,ord)
		ord.empresa=empresaInstance
		ord.fechaAlta=new Date()	
    	if(ord.validate()){
    		return ord.save();
    	}else{
    		def message=null
    		ord.errors.allErrors.each{
    			message+=it.toString()
    		}
    		throw new OrdenReservaException(message,ord);
    	}
    		
    }
    
    boolean anularOrdenReserva(Long id){
    	def ordenReservaInstance = OrdenReserva.get(id)
    	if (ordenReservaInstance)
    		ordenReservaInstance.anulada = true
    	else
    		throw new OrdenReservaException("No se pudo anular la orden de reserva. Orden inexistente, $id",ordenReservaInstance)
    	
    }
}

