package com.rural
//http://www.2paths.com/2009/10/01/one-to-many-relationships-in-grails-forms/
import com.rural.seguridad.*;

class Empresa {
	//datos comerciales
	String nombreRepresentante
	String direccion
	String email
	String nombre//es el nombre comercial
	String telefono1
	String telefono2
	String cargoRep//cargo representante
	String dniRep
	String sitioWeb
	String observaciones
	
	String token //token para tomar las empresas similares
	Integer totalsimilares
	
	//datos fiscales para la facturación
	String cuit
	String razonSocial //apellido y nombre del expositor o razón social
	String direccionFiscal
	String localidadFiscal
	String provinciaFiscal
	String codigoPostal
	String pais
	String telefonoFiscal
	
	Date fechaAlta
	Vendedor vendedor
	Localidad localidad
	SubRubro subrubro
	Person usuario
	String telefonoRepresentante1;
	String telefonoRepresentante2;
	String telefonoRepresentante3;
	
	
	
	static hasMany = [expos:Exposicion,exposaparticipar:Exposicion,empresas:Empresa]//empresas contiene todas las empress con nombre parecido
	
/*	static mapping = {
			empresas cascade:'delete-orphan'
			exposaparticipar cascade:'delete-orphan'

	}
	*/
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
		//usuario(blank:true,nullable:true)
		
		fechaAlta(blank:true,nullable:true)
		dniRep(blank:true,nullable:true)
		cargoRep(blank:true,nullable:true)
		sitioWeb(blank:true,nullable:true)
		observaciones(blank:true,nullable:true)
		razonSocial(blank:true,nullable:true)
		direccionFiscal(blank:true,nullable:true)
		
		localidadFiscal(blank:true,nullable:true)
		provinciaFiscal(blank:true,nullable:true)
		pais(blank:true,nullable:true)
		telefonoFiscal(blank:true,nullable:true)
		token(blank:true,nullable:true)
		totalsimilares(blank:true,nullable:true)
		email(blank:true,nullable:true)
				
    }
	static belongs = [vendedor:Vendedor, localidad:Localidad, subrubro: SubRubro,usuario:Person]
	
	
}
