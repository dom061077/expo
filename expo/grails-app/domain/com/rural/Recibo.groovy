package com.rural

import com.rural.seguridad.Person

class Recibo {
	Integer puntoVenta
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
		puntoVenta(blank:true,nullable:true)
    }
	
	static belongsTo = [ordenReserva:OrdenReserva,usuario:Person]
	
	static hasMany = [cheques:Cheque]

    def sigNumero(){
    	/*def c = Recibo.createCriteria()
    	def lastNum = c.get{
    		projections{
    			max("numero")
    		}
    		
    	}
		return lastNum ? lastNum+1 : 1   */ 	
		def max = OrdenReserva.executeQuery("select max(numero)+1 from Recibo r where r.ordenReserva.expo = ?",[ordenReserva.expo])[0]
		if (max == null) {
			max = 1
		}
		return max
    }
    
    def beforeInsert={
    	numero = sigNumero()
		puntoVenta = ordenReserva.puntoVenta
    }
	
	
}
