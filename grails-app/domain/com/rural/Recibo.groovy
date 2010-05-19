package com.rural

class Recibo {
	Long numero
	Date fechaAlta
	Double total
	OrdenReserva ordenReserva
    static constraints = {
		numero(blank:true,nullable:true)
    }
	
	static belongsTo = [ordenReserva:OrdenReserva]

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
