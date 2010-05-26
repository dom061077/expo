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

    

    OrdenReserva generarOrdenReserva(OrdenReserva ord,Empresa empresa) {
    	def empresaInstance = empresa.save()
    	ord.detalle.each{
    		ord.subTotal=ord.subTotal+it.subTotal
    	}
    	
    	ord.otrosconceptos.each{
    		ord.subTotal+=it.subTotal
    	}
    	if (empresaInstance==null)
    		throw new OrdenReservaException(message,ord)
    		
    	log.debug("PORCENTAJE ResIns ANTES DEL CALCULO")
    	log.debug("PROCENTAJE ResNoIns ANTES DEL CALCULO")
    	ord.ivaGral = ord.subTotal*ord.porcentajeResIns/100
    	ord.ivaRni = ord.subTotal*ord.porcentajeResNoIns/100	
    	ord.total=ord.subTotal+ord.ivaGral+ord.ivaRni	
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

