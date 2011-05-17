package com.rural

import java.sql.Date

class ListaPrecios {
	Double precio
	Date vigencia
	Sector sector
	Lote lote
	Exposicion expo
    static constraints = {
		sector(nullable:true,blank:false)
		lote(nullable:true,blank:false)
		expo(nullable:false,blank:false)
    }
	
	
}
