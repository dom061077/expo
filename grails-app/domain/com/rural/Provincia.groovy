package com.rural

class Provincia {
	String nombre
	static hasMany = [departamentos:Departamento]
    static constraints = {
    }
}
