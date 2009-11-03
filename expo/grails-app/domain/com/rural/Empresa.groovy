package com.rural

class Empresa {
	
	String nombreRepresentante
	String nombre
	String telefono1
	String telefono2
	String cuit
	String direccion
	String localidad
	String provincia
	Vendedor vendedor
	
    static constraints = {
    }
	static belongs = [vendedor:Vendedor]
}
