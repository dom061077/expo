package com.rural

import java.util.Locale


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
    	ord.ivaGral = ord.subTotal*(ord.porcentajeResIns > 0 ? ord.porcentajeResIns : ord.porcentajeResNoIns)/100
    	//ord.ivaRni = ord.subTotal*ord.porcentajeResNoIns/100	
    	ord.total=ord.subTotal+ord.ivaGral+ord.ivaRni
    	ord.total=Math.round(ord.total*Math.pow(10,2))/Math.pow(10,2);
		ord.empresa=empresaInstance
		ord.fechaAlta=new Date()	
    	if(ord.validate()){
    		ord = ord.save(); 
    		return ord
    		
    	}else{
    	
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
    
    boolean anularOrdenReserva(Long id){
    	def ordenReservaInstance = OrdenReserva.get(id)
    	if (ordenReservaInstance){
    		ordenReservaInstance.anulada = true
    		ordenReservaInstance.save()
    	}else
    		throw new OrdenReservaException("No se pudo anular la orden de reserva. Orden inexistente, $id",ordenReservaInstance)
    	
    }
}

