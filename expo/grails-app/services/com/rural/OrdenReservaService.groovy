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

    

    def generarOrdenReserva(OrdenReserva ord,Empresa empresa) {
    	def empresaInstance = empresa.save()
    	if (empresaInstance==null)
    		throw new OrdenReservaException(message,ord)
		ord.empresa=empresaInstance	
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
}
