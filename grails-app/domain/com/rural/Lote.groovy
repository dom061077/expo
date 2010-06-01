package com.rural

class Lote {
	String nombre
	Exposicion expo
	static belongsTo = [expo:Exposicion]
	static hasMany = [sectores:Sector]
    static constraints = {
    }
}
