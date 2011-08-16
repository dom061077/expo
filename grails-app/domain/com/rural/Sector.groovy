package com.rural

class Sector {
	static auditable = true
	
	String nombre
	Exposicion expo
	Double precio
	static belongsTo = [expo:Exposicion]
	static hasMany = [lotes:Lote,descuentos:ListaDescuentos]
    static constraints = {
		precio(nullable:true,blank:true)
		
    }
	
	static mapping = {
		descuentos(sort:'fechaVencimiento')
	}
}
