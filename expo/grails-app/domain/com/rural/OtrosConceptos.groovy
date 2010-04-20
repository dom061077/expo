package com.rural

class OtrosConceptos {
	
	String descripcion
	Double subTotal=0
	OrdenReserva ordenReserva
	TipoConcepto tipo
	
	static belongs = [ordenReserva:OrdenReserva,tipo:TipoConcepto]

    static constraints = {
    }
    
}
