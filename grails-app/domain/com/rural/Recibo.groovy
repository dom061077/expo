package com.rural

class Recibo {
	Long numero
	Date fechaAlta
	Boolean anulado = false
	Double total
	Double efectivo
	String concepto
	OrdenReserva ordenReserva
    static constraints = {
		numero(blank:true,nullable:true)
    }
	
	static belongsTo = [ordenReserva:OrdenReserva]
	
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
