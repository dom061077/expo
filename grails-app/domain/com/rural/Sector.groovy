package com.rural

class Sector {
	String nombre
	static belongsTo = [lote:Lote]
    static constraints = {
    }
}
