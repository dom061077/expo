package com.rural

class Cheque {
	String numero
	String banco
	Double importe
	Recibo recibo
	static belongsTo = [recibo:Recibo]
	
    static constraints = {
    }
}
