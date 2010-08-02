package com.rural.utils;

import java.util.Comparator;
import com.rural.DetalleServicioContratado;

public class LoteComparator implements Comparator{

	@Override
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		String nombre1="-";
		String nombre2="-";
		if(o1 instanceof DetalleServicioContratado)
			nombre1=o1.lote?.nombre
		if(o2 instanceof DetalleServicioContratado)
			nombre2=o2.lote?.nombre
		if(nombre1==null || nombre2==null){
			if(nombre1==null)
				return -1	
			if(nombre2==null)
				return 1
		}else
			return nombre1.compareTo(nombre2)
			
	}

}
