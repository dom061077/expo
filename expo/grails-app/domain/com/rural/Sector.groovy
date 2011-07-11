package com.rural

class Sector {
	String nombre
	Exposicion expo
	Double porcentaje
	Double precio
	static belongsTo = [expo:Exposicion]
	static hasMany = [lotes:Lote]
    static constraints = {
		porcentaje(nullable:true,blank:true)
    }
}
