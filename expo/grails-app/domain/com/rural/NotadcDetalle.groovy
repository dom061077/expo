package com.rural

class NotadcDetalle {
	String descripcion
	Double subTotal
	NotaDC notadc
	static belongsTo = [notadc:NotaDC]
    static constraints = {
    }
}
