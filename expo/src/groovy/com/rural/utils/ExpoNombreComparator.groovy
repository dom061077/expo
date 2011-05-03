package com.rural.utils

import com.rural.OrdenReserva
import com.rural.DetalleServicioContratado
import java.util.Comparator


class ExpoNombreComparator implements Comparator {
	
	public int compare(Object o1, Object o2){
		String nombre1=""
		String nombre2=""
		if(o1 instanceof DetalleServicioContratado)
			nombre1=o1.ordenReserva.expo.nombre
		else
			nombre1=o1.expo.nombre
		if(o2 instanceof DetalleServicioContratado)
			nombre2=o2.ordenReserva.expo.nombre
		else
			nombre2=o2.expo.nombre
		return nombre1.compareTo(nombre2)
	}


}
