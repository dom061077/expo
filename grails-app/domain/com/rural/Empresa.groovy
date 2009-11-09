package com.rural

class Empresa {
	
	String nombreRepresentante
	String nombre
	String telefono1
	String telefono2
	String cuit
	String direccion
	//String localidad
	String provincia
	Vendedor vendedor
	Localidad localidad
	
    static constraints = {
		vendedor(blank:true,nullable:true)
		localidad(blank:true,nullable:true)
    }
	static belongs = [vendedor:Vendedor, localidad:Localidad]
}
