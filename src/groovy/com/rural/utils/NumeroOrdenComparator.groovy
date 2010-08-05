package com.rural.utils

import java.util.Comparator
import com.rural.DetalleServicioContratado

class NumeroOrdenComparator implements Comparator{
	public int compare(Object o1, Object o2){
		Long numero1
		Long numero2
		if (o1 instanceof DetalleServicioContratado)
			numero1=o1.ordenReserva.numero
		else
			numero1=o1.numero[0]
		if (o2 instanceof DetalleServicioContratado)
			numero2=o2.ordenReserva.numero
		else
			numero2=o2.numero[0]
		return numero1.compareTo(numero2)
	}
}