package com.rural

class EmpresaException extends RuntimeException{
	String message
	Empresa empresa

	
	public EmpresaException(String message,Empresa emp){
		super(message)
		this.message=message
		this.empresa=empresa
	}
}

class EmpresaService {

    boolean transactional = true

    def generarOrdenReserva(Empresa emp) {
    	if(emp.validate()){
    		return emp.save();
    	}else{
    		def message=null
    		emp.errors.allErrors.each{
    			message+=it.toString()
    		}
    		throw new EmpresaException(message,emp);
    	}
    	

    }
}
