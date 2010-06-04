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
    	
    	ord.otrosconceptos.each{
    		ord.subTotal+=it.subTotal
    	}
    	if (!empresa.validate()){
    		throw new OrdenReservaException("Ocurrio un error al tratar de salvar los datos de la empresa. "
    			+empresa.errors.allErrors,ord)
   		}
    		def empresaInstance = empresa.save()
	    ord.detalle.each{
    			ord.subTotal=ord.subTotal+it.subTotal
    			if (ord.expo!=it.lote.sector.expo)
	    				throw new OrdenReservaException("Sector asignado no pertenece a la Exposición",ord)
    		}
    		
    	log.debug("PORCENTAJE ResIns ANTES DEL CALCULO")
    	log.debug("PROCENTAJE ResNoIns ANTES DEL CALCULO")
    	ord.ivaGral = ord.subTotal*ord.porcentajeResIns/100
    	ord.ivaRni = ord.subTotal*ord.porcentajeResNoIns/100	
    	ord.total=ord.subTotal+ord.ivaGral+ord.ivaRni	
		ord.empresa=empresaInstance
		ord.fechaAlta=new Date()	
    	if(ord.validate()){
    		ord = ord.save();
    		return ord
    		
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

