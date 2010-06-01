package com.rural



class DetalleServicioContratado {

	Double subTotal=0
	Sector sector
	
	OrdenReserva ordenReserva
	static belongs = [ordenReserva:OrdenReserva,sector:Sector]
	
    static constraints = {
    	sector(blank:false,nullable:false)
    }
}
