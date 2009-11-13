package com.rural

class Localidad {
	String nombreLoc
	Departamento departamento
	static belongs = [departamento:Departamento]
    static constraints = {
    }
}
