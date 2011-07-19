package com.rural

import java.sql.Date

class ListaDescuentos {
	Sector sector
	Double porcentaje
	Date fechaVencimiento
	
	static belongsTo = [sector:Sector]
	
    static constraints = {
		fechaVencimiento unique:'sector'
		porcentaje (min:1d,max:100d)
    }
}