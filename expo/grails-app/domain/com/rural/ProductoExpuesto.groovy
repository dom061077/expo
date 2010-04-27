package com.rural

class ProductoExpuesto {

	String descripcion
	OrdenReserva ordenReserva
	
	static belongs = [ordenReserva:OrdenReserva]

    static constraints = {
    }
}
