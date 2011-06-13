package com.rural

class Lote {
	String nombre
	Sector sector
	Double precio
	static belongsTo = [sector:Sector]
    static constraints = {
		precio(blank:true,nullable:true)
    }
}
