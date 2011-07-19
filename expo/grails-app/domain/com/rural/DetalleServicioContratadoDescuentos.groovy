package com.rural

class DetalleServicioContratadoDescuentos {
	Double subTotal=0
	Double porcentaje
	
	static belongsTo = [detalleServicioContratado:DetalleServicioContratado]
    static constraints = {
    }
}
