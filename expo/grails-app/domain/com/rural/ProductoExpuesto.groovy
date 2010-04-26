package com.rural

class ProductosExpuestos {

	String descripcion
	OrdenReserva ordenReserva
	
	static belongs = [ordenReserva:OrdenReserva]

    static constraints = {
    }
}
