package com.rural



class DetalleServicioContratado {

	String sector
	String lote
	Double subTotal=0
	
	OrdenReserva ordenReserva
	static belongs = [ordenReserva:OrdenReserva]
	
    static constraints = {
    }
}
