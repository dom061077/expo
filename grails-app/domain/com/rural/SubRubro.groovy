package com.rural

class SubRubro {
	
	String nombreSubrubro
	Rubro rubro
    static constraints = {
		nombreSubrubro(unique:true,nullable:false,blank:false)
    }
    static belongs = [rubro:Rubro]
	static hasMany = [empresas:Empresa]
}
