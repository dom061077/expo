package com.rural

class Provincia {
	String nombre
	static hasMany = [localidades:Localidad]
    static constraints = {
    }
}
