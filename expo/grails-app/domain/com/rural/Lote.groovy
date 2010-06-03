package com.rural

class Lote {
	String nombre
	Sector sector
	static belongsTo = [sector:Sector]
    static constraints = {
    }
}
