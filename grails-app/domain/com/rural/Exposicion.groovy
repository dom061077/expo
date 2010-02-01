package com.rural

class Exposicion {
	String nombre
	
	static hasMany = [empresas : Empresa]
    static constraints = {
    }
	
	static belongsTo = [Empresa]
}
