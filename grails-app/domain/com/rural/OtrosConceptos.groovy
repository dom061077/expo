package com.rural

class OtrosConceptos {
	
	String descripcion
	Double subtotal
	OrdenReserva ordenReserva
	TipoConcepto tipo
	
	static belongs = [ordenReserva:OrdenReserva,tipo:TipoConcepto]

    static constraints = {
    }
    
}
