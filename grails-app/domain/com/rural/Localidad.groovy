package com.rural

class Localidad {
	String nombreLoc
	Integer codigoPostal
	Provincia provincia
	static belongsTo = [provincia:Provincia]
    static constraints = {
		nombreLoc(blank:false)
    }
}
