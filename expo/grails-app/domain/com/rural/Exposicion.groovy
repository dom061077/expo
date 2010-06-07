package com.rural

class Exposicion {
	String nombre
	byte[] image

	static hasMany = [empresas:Empresa,sectores:Sector]

    static constraints = {
		image(blank:true,nullable:true)
    }
	
	static belongsTo = [Empresa]

	
/*	def beforeDelete = {
		empresaParent.exposaparticipar.remove(this)
		
	}*/
	
}
