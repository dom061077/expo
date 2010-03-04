package com.rural

class Empresa {
	
	String nombreRepresentante
	String nombre
	String telefono1
	String telefono2
	String cuit
	String direccion
	String codigoPostal
	Date fechaAlta
	
	//String localidad
	
	Vendedor vendedor
	Localidad localidad
	SubRubro subrubro
	String telefonoRepresentante1;
	String telefonoRepresentante2;
	String telefonoRepresentante3;
	
	static hasMany = [expos:Exposicion,exposaparticipar:Exposicion]
	
	
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
		telefonoRepresentante1(blank:true,nullable:true)
		telefonoRepresentante2(blank:true,nullable:true)
		telefonoRepresentante3(blank:true,nullable:true)		
		subrubro(blank:true,nullable:true)
		fechaAlta(blank:true,nullable:true)
    }
	static belongs = [vendedor:Vendedor, localidad:Localidad, subrubro: SubRubro]
}
