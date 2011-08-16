package com.rural

class Lote {
	static auditable = true
	
	String nombre
	Sector sector
	Double precio
	static belongsTo = [sector:Sector]
    static constraints = {
		precio(blank:true,nullable:true)
    }
}
