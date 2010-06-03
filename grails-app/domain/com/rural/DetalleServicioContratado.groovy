package com.rural



class DetalleServicioContratado {

	Double subTotal=0
	Lote lote
	
	OrdenReserva ordenReserva
	static belongs = [ordenReserva:OrdenReserva,lote:Lote]
	
    static constraints = {
    	lote(blank:false,nullable:false)
    }
}
