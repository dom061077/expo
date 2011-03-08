package com.rural

class Exposicion {
	String nombre
	Integer puntoVenta
	byte[] image

	static hasMany = [empresas:Empresa,sectores:Sector,logos:Logo]

    static constraints = {
		image(blank:true,nullable:true,maxSize:1024*30)
		puntoVenta(nullable:true,blank:true)
    }
	
	static belongsTo = [Empresa]
	
/*	def beforeDelete = {
		empresaParent.exposaparticipar.remove(this)
		
	}*/
	
	
}
