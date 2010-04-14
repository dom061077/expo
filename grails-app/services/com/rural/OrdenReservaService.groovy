package com.rural

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

    def generarOrdenReserva(OrdenReserva ord,Empresa empresa) {
    	def empresaInstance = empresa.save()
    	if(ord.validate()){
    		ord.empresa=empresaInstance	
    		return ord.save();
    	}else{
    		def message=null
    		ord.errors.allErrors.each{
    			message+=it.toString()
    		}
    		throw new OrdenReservaException(message,ord);
    	}
    		
    }
}
