package com.rural

import java.sql.Date

class ListaPrecios {
	static auditable = true
	
	Double precio
	Integer anio
	Sector sector
	Lote lote
	Exposicion expo
    static constraints = {
		anio(nullable:true,blank:true)
		sector(nullable:true,blank:true)
		lote(nullable:true,blank:true)
		expo(nullable:true,blank:true)
    }
	
	
}
