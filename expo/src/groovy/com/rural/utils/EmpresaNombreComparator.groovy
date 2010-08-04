package com.rural.utils

import com.rural.OrdenReserva
import java.util.Comparator

class EmpresaNombreComparator implements Comparator{
	public int compare(Object o1, Object o2){
		return o1.empresa.nombre.compareTo(o2.empresa.nombre)
	}
}