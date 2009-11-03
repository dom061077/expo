package com.rural

class Departamento {
	String nombre
	Provincia provincia
	static hasMany = [localidades:Localidad]
	static belongs = [provincia:Provincia]
    static constraints = {
    }
}
