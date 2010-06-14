package com.rural

class Exposicion {
	String nombre
	byte[] image

	static hasMany = [empresas:Empresa,sectores:Sector]

    static constraints = {
		image(blank:true,nullable:true,maxSize:1024*30)
		
    }
	
	static belongsTo = [Empresa]
	
/*	def beforeDelete = {
		empresaParent.exposaparticipar.remove(this)
		
	}*/
	
}
