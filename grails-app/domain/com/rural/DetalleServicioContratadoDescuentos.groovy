package com.rural

import java.sql.Date

class DetalleServicioContratadoDescuentos {
	Double subTotal=0
	Double porcentaje
	Date fechaVencimiento
	DetalleServicioContratado detalleServicioContratado
	NotadcDetalle notadcDetalle
	
	static belongsTo = [detalleServicioContratado:DetalleServicioContratado]
    static constraints = {
		fechaVencimiento( unique:'detalleServicioContratado')
		porcentaje(min:1d,max:100d)
		detalleServicioContratado(nullable:true)
		notadcDetalle(nullable:true)
    }
}
