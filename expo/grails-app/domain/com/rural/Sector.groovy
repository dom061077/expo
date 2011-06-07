package com.rural

class Sector {
	String nombre
	Exposicion expo
	Double precio
	static belongsTo = [expo:Exposicion]
	static hasMany = [lotes:Lote]
    static constraints = {
		precio(nullable:true,blank:true)
    }
}
