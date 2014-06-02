package com.rural

import java.sql.Date
import java.text.SimpleDateFormat

class DetalleServicioContratadoDescuentos {
	Double subTotal=0
	Double porcentaje
	Double porcentajeActual
	Double porcentajeSig
	Date fechaVencimiento
	DetalleServicioContratado detalleServicioContratado
	NotadcDetalle notadcDetalle
	
	static transients = ['leyendaDesc','montoDescontado']

    Double getMontoDescontado(){
        detalleServicioContratado.subTotal-subTotal
    }

	String getLeyendaDesc(){
		def format = new SimpleDateFormat("dd/MM/yyyy")
		def strDate = format.format(fechaVencimiento.getTime())
		def numFormat = String.format("%.2f", porcentaje);
		return "Descuento del "+numFormat+" válido hasta "+strDate;
	}
	
	static belongsTo = [detalleServicioContratado:DetalleServicioContratado]
    static constraints = {
		fechaVencimiento( unique:'detalleServicioContratado')
		porcentaje(min:1d,max:100d)
		porcentajeSig(nullable:true)
		porcentajeActual(nullable:true)
		detalleServicioContratado(nullable:true)
		notadcDetalle(nullable:true)
    }
}
