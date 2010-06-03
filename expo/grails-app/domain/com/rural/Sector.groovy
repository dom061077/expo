package com.rural

class Sector {
	String nombre
	Exposicion expo
	static belongsTo = [expo:Exposicion]
	static hasMany = [lotes:Lote]
    static constraints = {
    }
}
