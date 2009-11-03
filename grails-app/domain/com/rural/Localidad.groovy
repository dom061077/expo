package com.rural

class Localidad {
	String nombre
	Departamento departamento
	static belongs = [departamento:Departamento]
    static constraints = {
    }
}
