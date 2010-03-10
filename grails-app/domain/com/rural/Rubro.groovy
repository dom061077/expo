package com.rural

class Rubro {

	String nombreRubro
    static constraints = {
		nombreRubro(unique:true,nullable:false,blank:false)
		
		
		
    }
	static hasMany = [subRubros : SubRubro]
}
