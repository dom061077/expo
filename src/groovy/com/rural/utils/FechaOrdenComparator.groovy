package com.rural.utils

import com.rural.OrdenReserva
import java.util.Comparator

class FechaOrdenComparator implements Comparator{
	public int compare(Object o1, Object o2){
		return o1.fechaAlta.compareTo(o2.fechaAlta)
	}
}