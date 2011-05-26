package com.rural

import java.sql.Date

class ListaPrecios {
	Double precio
	Date vigencia
	Sector sector
	Lote lote
	Exposicion expo
    static constraints = {
		sector(nullable:true,blank:true)
		lote(nullable:true,blank:true)
		expo(nullable:false,blank:false)
    }
	
	
}
