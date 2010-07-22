package com.rural



class DetalleServicioContratado {

	Double subTotal=0
	Lote lote
	Sector sector
	OrdenReserva ordenReserva
	static belongs = [ordenReserva:OrdenReserva,lote:Lote,sector:Sector]
	
    static constraints = {
    	lote(blank:true,nullable:true)
    	sector(blank:true,nullable:true)
    }
}
