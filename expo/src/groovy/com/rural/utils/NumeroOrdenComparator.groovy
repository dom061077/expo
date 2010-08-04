package com.rural.utils

import java.util.Comparator

class NumeroOrdenComparator implements Comparator{
	public int compare(Object o1, Object o2){
		return o1.numero.compareTo(o2.numero)
	}
}