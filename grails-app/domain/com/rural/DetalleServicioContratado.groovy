package com.rural



class DetalleServicioContratado {

	Double subTotal=0
	Double subTotalsindesc=0
	Lote lote
	Sector sector
	OrdenReserva ordenReserva
	static belongs = [ordenReserva:OrdenReserva,lote:Lote,sector:Sector]
	
	static hasMany = [descuentos:DetalleServicioContratadoDescuentos]
	
    static constraints = {
    	lote(blank:true,nullable:true)
    	sector(blank:true,nullable:true)
		subTotalsindesc(blank:true,nullable:true)
    }
	
	static mapping={
		descuentos(sort:'fechaVencimiento',order:'asc')
	}
	
}
