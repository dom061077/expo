package com.rural.utils

import com.rural.OrdenReserva
import com.rural.DetalleServicioContratado
import java.util.Comparator
import java.sql.Date

class FechaOrdenComparator implements Comparator{
	public int compare(Object o1, Object o2){
		Date fecha1
		Date fecha2
		if(o1 instanceof DetalleServicioContratado)
			fecha1=o1.ordenReserva.fechaAlta
		else	
			fecha1=o1.fechaAlta
			
		if(o2 instanceof DetalleServicioContratado)
			fecha2=o2.ordenReserva.fechaAlta
		else
			fecha2=o2.fechaAlta
		return fecha1.compareTo(fecha2)
	}
}