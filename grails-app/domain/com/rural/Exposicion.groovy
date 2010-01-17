package com.rural

class Exposicion {
	String nombre
	
	static belongsTo = [empresa : Empresa]
    static constraints = {
    }
}
