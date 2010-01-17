package com.rural

class Empresa {
	
	String nombreRepresentante
	String nombre
	String telefono1
	String telefono2
	String cuit
	String direccion
	String codigoPostal
	
	//String localidad
	
	Vendedor vendedor
	Localidad localidad
	String telefonoRepresentante1;
	String telefonoRepresentante2;
	String telefonoRepresentante3;
	
	static hasMany = [expos:Exposicion]
	
	
    static constraints = {
		nombre(unique:true)
		vendedor(blank:true,nullable:true)
		localidad(blank:true,nullable:true)
		codigoPostal(blank:true,nullable:true)
		nombreRepresentante(blank:true,nullable:true)
		telefono1(blank:true,nullable:true)
		telefono2(blank:true,nullable:true)
		cuit(blank:true,nullable:true)
		direccion(blank:true,nullable:true)
		
    }
	static belongs = [vendedor:Vendedor, localidad:Localidad]
}
