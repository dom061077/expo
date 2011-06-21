package com.rural




import com.rural.seguridad.*
import java.sql.Date
import com.rural.enums.TipoNotaEnum



class OrdenReserva {
	static auditable = true
	
	Empresa empresa
	Person usuario
	Exposicion expo
	Boolean anulada = false
	Double ivaGral=0
	Double ivaRni=0/*es el resultado subtotal neto + ivaGral*/
	Double ivaSujNoCateg=0 /*es mayor a cero cuando la condicion de IVA es ivaRniCheck true*/
	Double subTotal=0
	Double subTotalsindesc=0
	Double total=0
	Double totalsindesc=0
	Integer anio
	Boolean ivaGralCheck = false
	Boolean ivaRniCheck = false
	Boolean exentoCheck = false
	Boolean consFinalCheck = false
	Boolean monotributoCheck = false
	Float porcentajeResIns=0
	Float porcentajeResNoIns=0
	String observacion
	Double subtotalDetalle
	Double subtotalOtrosConceptos
	Double debito
	Double credito
	
	Long numero
	Date fechaAlta
	Date fechaVencimiento
	
	Integer puntoVenta
	
	//-------datos persistidos del expositor----
	String nombreRepresentante
	String direccion
	String email
	String nombre//es el nombre comercial
	String telefono1
	String telefono2
	String cargoRep//cargo representante
	String dniRep
	
	
	//datos fiscales para la facturación
	String cuit
	String razonSocial //apellido y nombre del expositor o razón social
	String direccionFiscal
	String localidadFiscal
	String provinciaFiscal
	String codigoPostal
	String pais
	String telefonoFiscal
	
	
	Vendedor vendedor
	Localidad localidad
	
	
	String telefonoRepresentante1;
	String telefonoRepresentante2;
	String telefonoRepresentante3;
	
	
	//-------fin datos persistidos del expositor-----
	
	
	static belongsTo = [empresa:Empresa,usuario:Person,expo:Exposicion] 

	static hasMany = [detalle:DetalleServicioContratado,otrosconceptos:OtrosConceptos,productos:ProductoExpuesto,recibos:Recibo,notas:NotaDC]
	
    static constraints = {
    	numero(blank:true,nullable:true)
		puntoVenta(blank:true,nullable:true)
		nombreRepresentante(blank:true,nullable:true)
		direccion(blank:true,nullable:true)
		email(blank:true,nullable:true)
		nombre(blank:true,nullable:true)
		telefono1(blank:true,nullable:true)
		telefono2(blank:true,nullable:true)
		cargoRep(blank:true,nullable:true)
		dniRep(blank:true,nullable:true)
		cuit(blank:true,nullable:true)
		razonSocial(blank:true,nullable:true) //apellido y nombre del expositor o razón social
		direccionFiscal(blank:true,nullable:true)
		localidadFiscal(blank:true,nullable:true)
		provinciaFiscal(blank:true,nullable:true)
		codigoPostal(blank:true,nullable:true)
		pais(blank:true,nullable:true)
		telefonoFiscal(blank:true,nullable:true)
		
		puntoVenta(blank:true,nullable:true)
		
		vendedor(blank:true,nullable:true)
		localidad(blank:true,nullable:true)
		
		
		telefonoRepresentante1(blank:true,nullable:true)
		telefonoRepresentante2(blank:true,nullable:true)
		telefonoRepresentante3(blank:true,nullable:true)
		/*--propiedades agregadas para aplicar los descuentos y el tarifario--*/
		subTotalsindesc(blank:true,nullable:true)
		fechaVencimiento(blank:true,nullable:true)
    }
		   
    
    static mapping = {
    	subtotalDetalle formula:"(select sum(d.sub_total) from detalle_servicio_contratado d where d.orden_reserva_id=id)"
		subtotalOtrosConceptos formula:"(select sum(o.sub_total) from otros_conceptos o where o.orden_reserva_id=id)"
		debito formula:"(select sum(ndc.total) from notadc ndc where ndc.orden_reserva_id=id and ndc.anulada=false and ndc.tipo='"+TipoNotaEnum.NOTA_DEBITO+"')"
		credito formula:"(select sum(ndc.total) from notadc ndc where ndc.orden_reserva_id=id and ndc.anulada=false and ndc.tipo='"+TipoNotaEnum.NOTA_CREDITO+"')"
    }
    
    def sigNumero(){
    	/*def c = OrdenReserva.createCriteria()
    	def lastNum = c.get{
    		projections{
    			max("numero")
    		}
    		
    	}
		return lastNum ? lastNum+1 : 1    */
		def max = OrdenReserva.executeQuery("select max(numero)+1 from OrdenReserva o where o.expo = ?",[expo])[0]
		if (max == null) {
			max = 1
		}
		return max

    }
    
    def beforeInsert={
    	numero = sigNumero()
		
		nombreRepresentante=empresa.nombreRepresentante
		direccion=empresa.direccion
		email=empresa.email
		nombre=empresa.nombre//es el nombre comercial
		telefono1=empresa.telefono1
		telefono2=empresa.telefono2
		cargoRep=empresa.cargoRep//cargo representante
		dniRep=empresa.dniRep
		
		
		//datos fiscales para la facturación
		cuit=empresa.cuit
		razonSocial=empresa.razonSocial //apellido y nombre del expositor o razón social
		direccionFiscal=empresa.direccionFiscal
		localidadFiscal=empresa.localidadFiscal
		provinciaFiscal=empresa.provinciaFiscal
		codigoPostal=empresa.codigoPostal
		pais=empresa.pais
		telefonoFiscal=empresa.telefonoFiscal
		vendedor=empresa.vendedor
		localidad=empresa.localidad
		telefonoRepresentante1=empresa.telefonoRepresentante1
		telefonoRepresentante2=empresa.telefonoRepresentante2
		telefonoRepresentante3=empresa.telefonoRepresentante3
		puntoVenta=expo.puntoVenta
		
		
		
		
    }
    
}
