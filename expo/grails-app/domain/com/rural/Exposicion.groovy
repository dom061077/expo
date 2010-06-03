package com.rural

class Exposicion {
	String nombre

	static hasMany = [empresas:Empresa,sectores:Sector]

    static constraints = {
    }
	
	static belongsTo = [Empresa]

	
/*	def beforeDelete = {
		empresaParent.exposaparticipar.remove(this)
		
	}*/
	
}
