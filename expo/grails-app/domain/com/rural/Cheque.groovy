package com.rural

class Cheque {
	String numero
	String banco
	Double importe
	Recibo recibo
	Date vencimiento
	static belongsTo = [recibo:Recibo]
	 
    static constraints = {
    	vencimiento(blank:true,nullable:true)
    } 
}
