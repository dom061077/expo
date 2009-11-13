package com.rural

class Departamento {
	String nombreDep
	Provincia provincia
	static hasMany = [localidades:Localidad]
	static belongs = [provincia:Provincia]
    static constraints = {
    }
}
