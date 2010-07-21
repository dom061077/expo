package com.rural

import com.rural.seguridad.Person

class Recibo {
	Long numero
	Date fechaAlta
	Boolean anulado = false
	Double total
	Double efectivo
	String concepto
	OrdenReserva ordenReserva
	Person usuario
    static constraints = {
		numero(blank:true,nullable:true)
		usuario(blank:true,nullable:true)
    }
	
	static belongsTo = [ordenReserva:OrdenReserva,usuario:Person]
	
	static hasMany = [cheques:Cheque]

    def sigNumero(){
    	def c = Recibo.createCriteria()
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
