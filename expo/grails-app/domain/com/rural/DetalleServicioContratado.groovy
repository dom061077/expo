package com.rural



class DetalleServicioContratado {

	String sector
	String lote
	Double subtotal
	
	OrdenReserva ordenReserva
	static belongs = [ordenReserva:OrdenReserva]
	
    static constraints = {
    }
}
