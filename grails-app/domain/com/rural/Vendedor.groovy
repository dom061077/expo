package com.rural

class Vendedor {

	String nombre
	static hasMany = [empresas:Empresa]
    static constraints = {
		nombre(blank: false,unique:true)
    }
}
