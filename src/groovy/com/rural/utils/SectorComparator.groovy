package com.rural.utils

import java.util.Comparator
import com.rural.DetalleServicioContratado

class SectorComparator implements Comparator{
	
	public int compare(Object o1, Object o2){
		String nombre1=""
		String nombre2=""
		if(o1 instanceof DetalleServicioContratado)
			nombre1=o1.sector?.nombre 
		if(o2 instanceof DetalleServicioContratado)
			nombre2=o2.sector?.nombre
		if(nombre1==null)
			nombre1=""	
		if(nombre2==null)
			nombre2=""
		return nombre1.compareTo(nombre2)
		
	}
}