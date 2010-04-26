package com.rural

import com.rural.seguridad.*

class OrdenReserva {
	Empresa empresa
	Person usuario
	Exposicion expo
	Boolean anulada = false
	Double ivaGral=0
	Double ivaRni=0
	Double ivaSujNoCateg=0
	Double subTotal=0
	Double total=0
	
	
	Date fechaAlta
	static belongs = [empresa:Empresa,usuario:Person,expo:Exposicion] 

	static hasMany = [detalle:DetalleServicioContratado,otrosconceptos:OtrosConceptos,productos:ProductoExpuesto]
	
    static constraints = {
    }
    
    
    static mapping = {
    	
    }
}
