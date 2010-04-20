package com.rural

import com.rural.seguridad.*

class OrdenReserva {
	Empresa empresa
	Person usuario
	Exposicion expo
	Boolean anulada = false
	
	Date fechaAlta
	static belongs = [empresa:Empresa,usuario:Person,expo:Exposicion] 

	static hasMany = [detalle:DetalleServicioContratado,otrosconceptos:OtrosConceptos]
	
    static constraints = {
    }
    
    
    static mapping = {
    	
    }
}
