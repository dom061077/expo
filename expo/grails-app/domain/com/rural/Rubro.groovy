package com.rural

class Rubro {

	String nombre
	Rubro subRubro
    static constraints = {
		nombre(unique:true,nullable:false,blank:false)
		subRubro(nullable:true,blank:true)
		empresas(nullabel:true,blank:true)
    }
	static hasMany = [subRubro : Rubro,empresas : Empresa]
}
