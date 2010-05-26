package com.rural

import com.rural.seguridad.*

class OrdenReserva {
	Empresa empresa
	Person usuario
	Exposicion expo
	Boolean anulada = false
	Double ivaGral=0
	Double ivaRni=0
	Double ivaSujNoCateg=0
	Double subTotal=0
	Double total=0
	Integer anio
	Boolean ivaGralCheck = false
	Boolean ivaRniCheck = false
	Boolean exentoCheck = false
	Boolean consFinalCheck = false
	Boolean monotributoCheck = false
	Float porcentajeResIns=0
	Float porcentajeResNoIns=0
	
	Long numero
	Date fechaAlta
	static belongsTo = [empresa:Empresa,usuario:Person,expo:Exposicion] 

	static hasMany = [detalle:DetalleServicioContratado,otrosconceptos:OtrosConceptos,productos:ProductoExpuesto,recibos:Recibo]
	
    static constraints = {
    	numero(blank:true,nullable:true)
    }
    
    
    static mapping = {
    	
    }
    
    def sigNumero(){
    	def c = OrdenReserva.createCriteria()
    	def lastNum = c.get{
    		projections{
    			max("numero")
    		}
    		
    	}
		return lastNum ? lastNum+1 : 1    	
    }
    
    def beforeInsert={
    	numero = sigNumero()
    }
    
}
