package com.rural

class OtrosConceptos {
	
	String descripcion
	Double subtotal
	OrdenReserva ordenReserva
	
	
	static belongs = [ordenReserva:OrdenReserva]

    static constraints = {
    }
}
